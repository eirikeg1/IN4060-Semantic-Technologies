import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;

public class Oblig2 {

    static Model model = ModelFactory.createDefaultModel();

    // Namespaces
    static String xsd;
    static String rdf;
    static String sim;
    static String fam;
    static String foaf;

    // Datatypes
    static RDFDatatype age_type;
    static Resource person;
    static Resource infant;
    static Resource minor;
    static Resource old;

    // Properties
    static Property a;
    static Property name;
    static Property age;
    static Property hasSpouse;
    static Property hasFather;

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: java Oblig2 <input_file> <output_file>");
            System.exit(1);
        }

        String input_file = args[0];
        String file_ending = input_file.substring(input_file.lastIndexOf(".") + 1);
        String file_type = null;

        if (file_ending.equals("ttl")) {
            file_type = "TTL";
        } else if (file_ending.equals("rdf")) {
            file_type = "RDF/XML";
        } else {
            System.out.println("File type not supported, please use .ttl or .rdf files.");
            System.exit(1);
        }

        try {
            model.read(new FileInputStream(input_file), "", file_type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // System.out.println("Could not find file: " + input_file + ". Please check the
            // file path.");
            // System.exit(1);
        }

        setupEnvironment();

        task2();
        task3();

        String outFile = args[1];
        String outEnding = input_file.substring(input_file.lastIndexOf(".") + 1);
        String outType = null;

        if (outEnding.equals("ttl")) {
            outType = "TTL";
        } else if (outEnding.equals("rdf")) {
            outType = "RDF/XML";
        } else {
            System.out.println("File type not supported, please use .ttl or .rdf files.");
            System.exit(1);
        }

        try {
            model.write(new FileWriter(outFile), "TTL");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private void setupEnvironment() {

        // Access and print a specific namespace
        xsd = model.getNsPrefixURI("xsd");
        rdf = model.getNsPrefixURI("rdf");
        sim = model.getNsPrefixURI("sim");
        fam = model.getNsPrefixURI("fam");
        foaf = model.getNsPrefixURI("foaf");

        // Create properties
        age_type = XSDDatatype.XSDint;
        person = model.createResource(foaf + "Person");
        infant = model.createResource(fam + "Infant");
        minor = model.createResource(fam + "Minor");
        old = model.createResource(fam + "Old");

        a = RDF.type;
        name = model.createProperty(foaf + "name");
        age = model.createProperty(foaf + "age");
        hasSpouse = model.createProperty(fam + "hasSpouse");
        hasFather = model.createProperty(fam + "hasFather");
    }

    static private void task2() {
        // Characters
        Resource maggie = model.getResource(sim + "Maggie");
        Resource mona = model.getResource(sim + "Mona");
        Resource abraham = model.getResource(sim + "Abraham");
        Resource herb = model.getResource(sim + "Herb");

        maggie.addProperty(a, person);
        maggie.addProperty(name, "Maggie Simpson");
        maggie.addProperty(age, model.createTypedLiteral("1", age_type));

        mona.addProperty(a, person);
        mona.addProperty(name, "Mona Simpson");
        mona.addProperty(age, model.createTypedLiteral("70", age_type));

        abraham.addProperty(a, person);
        abraham.addProperty(name, "Abraham Simpson");
        abraham.addProperty(age, model.createTypedLiteral("78", age_type));

        abraham.addProperty(hasSpouse, mona);
        mona.addProperty(hasSpouse, abraham);

        herb.addProperty(a, person);
        herb.addProperty(hasFather, model.createResource());

    }

    static private void task3() {
        Iterator<Resource> people = model.listResourcesWithProperty(a, person);
        while (people.hasNext()) {
            Resource person = people.next();

            if (!person.hasProperty(age)) {
                continue;
            }

            int ageValue = person.getProperty(age).getInt();

            if (ageValue < 2) {
                person.addProperty(a, infant);
            }
            if (ageValue < 18) {
                person.addProperty(a, minor);
            }
            if (ageValue > 70) {
                person.addProperty(a, old);
            }
        }

    }
}