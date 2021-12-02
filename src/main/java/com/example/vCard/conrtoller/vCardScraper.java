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
import java.util.Arrays;
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

        StringBuilder[] vcfBuilder = new StringBuilder[companies.size()];

        for (int i = 0; i < companies.size(); i++) {
            vcfBuilder[i] = new StringBuilder("BEGIN:VCARD\r\n");
            vcfBuilder[i].append("VERSION:4.0\r\n");
            vcfBuilder[i].append("ORG:").append(companies.get(i).getName()).append("\r\n");
            vcfBuilder[i].append("TEL:").append(companies.get(i).getTelephone()).append("\r\n");
            vcfBuilder[i].append("ADR:").append(companies.get(i).getStreet()).append(" ").append(companies.get(i).getPostalCode()).append("\r\n");
            vcfBuilder[i].append("EMAIL:").append(companies.get(i).getEmail()).append("\r\n");
            vcfBuilder[i].append("END:VCARD\n");

            try {
                File file = new File("GeneratedCards/vcard" + i + ".vcf");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(vcfBuilder[i].toString().getBytes(StandardCharsets.UTF_8));
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }

        return Arrays.toString(vcfBuilder);
    }

    private String getFieldFromJSONString(String json, String field) {
        String str;
        int indexOf = json.indexOf(field);
        str = json.substring(indexOf + field.length() + 3, json.indexOf("\"", indexOf + field.length() + 3));
        return str;
    }
}
