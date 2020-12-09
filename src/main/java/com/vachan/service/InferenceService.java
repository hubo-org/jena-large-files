package com.vachan.service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("infService")
public class InferenceService {
	
	private static Logger log = LoggerFactory.getLogger(InferenceService.class);

	private static String path = getPath();

	private static String input_path = "/data/input/";

	private static String output_path = "/data/output/";

	private OntModel ontModel = loadOntModel();

	private Reasoner reasoner = ReasonerRegistry.getOWLReasoner();

	private static String getPath() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		return s;
	}

	private OntModel loadOntModel() {
		String resource = path + input_path + "harbour_politica_ontology_trimmed.owl";
		log.info("File: " + resource);
		OntModel ont_model = ModelFactory.createOntologyModel();
		try {
			ont_model.read(resource);
		} catch (Exception e) {

			log.error("Failed Loading Model from the owl file ", e);
		}

		return ont_model;
	}

	public long loadSingle() throws Exception {
		long start = System.currentTimeMillis();

		String resource = path + input_path + "input_sample.txt";
		log.info("File: " + resource);

		Model data = ModelFactory.createDefaultModel();
		data.read(resource, "TURTLE");
		InfModel inf = ModelFactory.createInfModel(reasoner, ontModel, data);
		log.info("Created Inferred Model");
		String out_file = path + output_path + "jena_output_sample.txt";
		OutputStream os = new FileOutputStream(out_file);

		generateInferredModel(data, inf, os);
		log.info("Completed");
		os.flush();
		os.close();

		long end = System.currentTimeMillis();
		long time_taken = end - start;
		log.info("Time taken:" + time_taken + "ms");
		return time_taken;
	}

	public long loadInputFile(String fileName) throws Exception {
		long start = System.currentTimeMillis();

		String resource = path + input_path + fileName;
		log.info("File: " + resource);

		Model data = ModelFactory.createDefaultModel();
		data.read(resource, "TURTLE");
		InfModel inf = ModelFactory.createInfModel(reasoner, ontModel, data);
		log.info("Created Inferred Model");
		String out_file = path + output_path + "jena_output_" + fileName;
		OutputStream os = new FileOutputStream(out_file);

		generateInferredModel(data, inf, os);
		log.info("Completed");
		os.flush();
		os.close();

		long end = System.currentTimeMillis();
		long time_taken = end - start;
		log.info("Time taken:" + time_taken + "ms");
		return time_taken;
	}

	public long loadFullFile() throws Exception {
		long start = System.currentTimeMillis();

		String resource = path + input_path + "input_full_data.txt";
		log.info("File: " + resource);

		Model data = ModelFactory.createDefaultModel();
		data.read(resource, "TURTLE");
		InfModel inf = ModelFactory.createInfModel(reasoner, ontModel, data);
		log.info("Created Inferred Model");
		String out_file = path + output_path + "jena_output_full_data.txt";
		OutputStream os = new FileOutputStream(out_file);

		generateInferredModel(data, inf, os);
		log.info("Completed");
		os.flush();
		os.close();

		long end = System.currentTimeMillis();
		long time_taken = end - start;
		log.info("Time taken:" + time_taken + "ms");
		return time_taken;
	}

	private void generateInferredModel(Model data, InfModel inf, OutputStream os) {
		log.info("Parsing RDF type");
		long pStart = System.currentTimeMillis();
		Property p = inf.getProperty("urn:absolute://harbourpolitica/ontology/uri");
		Model result = ModelFactory.createDefaultModel();
		ResIterator resIT = data.listResourcesWithProperty(RDF.type);

		while (resIT.hasNext()) {

			Resource rsc = resIT.next();
			Resource inf_resc = inf.getResource(rsc.getURI());
			result.add(inf_resc.listProperties(RDF.type).toModel());
			result.add(inf_resc.listProperties(p).toModel());
		}

		long pEnd = System.currentTimeMillis();
		log.info("Time taken to parse the RDF:" + (pEnd - pStart) + "ms");
		log.info("Write to file");
		long start = System.currentTimeMillis();
		RDFDataMgr.write(os, result, Lang.NTRIPLES);
		long end = System.currentTimeMillis();
		log.info("Time taken to write to file:" + (end - start) + "ms");

	}

}
