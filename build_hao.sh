#!/bin/bash

export CLASSPATH=.:lib/log4j-1.2.15.jar:lib/oboformat.jar:lib/owlapi-bin.jar

# need to check URL and also generate plain-text, not HTML
#curl -O http://peet.tamu.edu/projects/32/public/ontology/show_OBO_file
# this URL is just for testing
curl -L -O http://purl.obolibrary.org/obo/hao.obo
java -Xmx2048M org.obolibrary.cli.OBORunner hao.obo -o file://`pwd`/hao_base.owl
#TODO download image RDF from MX > hao_images.owl
#curl -L -O http://peet.tamu.edu/projects/32/public/ontology/export_class_depictions -o hao_images.owl
javac OWLFileMerger.java
java OWLFileMerger hao_base.owl hao_images.owl hao.owl
