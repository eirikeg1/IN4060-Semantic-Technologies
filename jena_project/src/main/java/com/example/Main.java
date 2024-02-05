package com.example;

import java.io.FileInputStream;
import java.util.Map;

import org.apache.jena.rdf.model.*;

public class Main {

    static Model model = ModelFactory.createDefaultModel();

    // Namespaces
    static String xsd;
    static String rdf;
    static String sim;
    static String fam;
    static String foaf;

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: Main <input_file>");
            System.exit(1);
        }

        String input_file = args[0];
        String file_ending = input_file.substring(input_file.lastIndexOf(".") + 1);
        String file_type;

        if (file_ending.equals("ttl")) {
            file_type = "TTL";
        } else if (file_ending.equals("rdf")) {
            file_type = "RDF/XML";
        } else {
            System.out.println("File type not supported, please use .ttl or .rdf files.");
            System.exit(1);
        }

        model.read(new FileInputStream(input_file), "", file_type);
        model.init_namespaces();

        model.task2();
    }

    private void init_namespaces() {

        // Get the namespaces from the model
        Map<String, String> prefixMap = model.getNsPrefixMap();
        for (Map.Entry<String, String> entry : prefixMap.entrySet()) {
            System.out.println("Prefix: " + entry.getKey() + " URI: " + entry.getValue());
        }

        // Access and print a specific namespace
        xsd = model.getNsPrefixURI("xsd");
        rdf = model.getNsPrefixURI("rdf");
        sim = model.getNsPrefixURI("sim");
        fam = model.getNsPrefixURI("fam");
        foaf = model.getNsPrefixURI("foaf");
    }

    private void task2() {
        // Characters
        Resource maggie = model.getResource(sim + "Maggie");
        Resource mona = model.getResource(sim + "Mona");
        Resource abraham = model.getResource(sim + "Abraham");
        Resource herb = model.getResource(sim + "Herb");

    }
}