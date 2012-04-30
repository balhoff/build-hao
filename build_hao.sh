#!/bin/bash

export CLASSPATH=.:lib/log4j-1.2.15.jar:lib/oboformat.jar:lib/owlapi-bin.jar

rm hao.obo hao_base.owl hao_images.owl hao.owl
curl -L -o hao.obo http://api.hymao.org/api/ontology/obo_file
java -Xmx2048M org.obolibrary.cli.OBORunner hao.obo -o hao_base.owl
curl -L -o hao_images.owl http://api.hymao.org/api/ontology/class_depictions
javac OWLFileMerger.java
java OWLFileMerger hao_base.owl hao_images.owl hao.owl
