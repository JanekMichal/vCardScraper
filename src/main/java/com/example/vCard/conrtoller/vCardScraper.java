package com.example.vCard.conrtoller;

import com.example.vCard.Model.Company;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
public class vCardScraper {

    @GetMapping("/vCard/{profession}")
    public String getData(@PathVariable("profession") String profession) throws IOException {

        String stringBuilder = "https://panoramafirm.pl/szukaj?k=" +
                profession +
                "&l=";
        Document doc = Jsoup.connect(stringBuilder).get();

        List<Company> companies = new ArrayList<>();
        Elements elements = doc.select("script");
        for (Element element : elements) {
            if (element.attr("type").equals("application/ld+json")) {
                if (element.data().contains("LocalBusiness")) {
                    Company company = new Company();
                    company.setName(getFieldFromJSONString(element.data(), "name"));
                    company.setTelephone(getFieldFromJSONString(element.data(), "telephone"));
                    company.setEmail(getFieldFromJSONString(element.data(), "email"));
                    company.setPostalCode(getFieldFromJSONString(element.data(), "postalCode"));
                    company.setCountry(getFieldFromJSONString(element.data(), "addressCountry"));
                    company.setStreet(getFieldFromJSONString(element.data(), "streetAddress"));
                    company.setCity(getFieldFromJSONString(element.data(), "addressLocality"));
                    companies.add(company);
                }
            }
        }

        StringBuilder vdfBuilder = new StringBuilder();
        for (Company selectedCompany :
                companies) {
            vdfBuilder.append("BEGIN:VCARD\r\n");
            vdfBuilder.append("VERSION:4.0\r\n");
            vdfBuilder.append("ORG:").append(selectedCompany.getName()).append("\r\n");
            vdfBuilder.append("TEL:").append(selectedCompany.getTelephone()).append("\r\n");
            vdfBuilder.append("ADR:").append(selectedCompany.getStreet()).append(" ").append(selectedCompany.getPostalCode()).append("\r\n");
            vdfBuilder.append("EMAIL:").append(selectedCompany.getEmail()).append("\r\n");
            vdfBuilder.append("END:VCARD\n");
        }


        try {
            File file = new File("vcard.vcf");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(vdfBuilder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        return vdfBuilder.toString();
    }

    private String getFieldFromJSONString(String json, String field) {
        String str;
        int indexOf = json.indexOf(field);
        str = json.substring(indexOf + field.length() + 3, json.indexOf("\"", indexOf + field.length() + 3));
        return str;
    }
}
