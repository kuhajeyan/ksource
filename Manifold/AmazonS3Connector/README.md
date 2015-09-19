# AmazonS3 Repository Connector


* Overview
	1. Connects to Amazons3 buckets, and indexes the artifact. if any buckets to be avoided it can be skipped ( it can be configured in job) 
	2. Internally documents are parsed and meta data are extracted using Tika
	3. Support Locale - English US ( Currently common_en_US.properties, available, looking for support from some to do the translation for the keys)
B. Documentation - Work in progress, will be attached issue on the following days

* Dependencies - (common-lib)
	1. aws-java-sdk-{version}.jar
	2. aws-java-sdk-core-{version}.jar
	3. aws-java-sdk-s3-{version}.jar
	4. joda-time-2.2.jar
D. Connectors.xml
	<!-- Add your authority connectors here -->
	<authorityconnector name="Amazons3" class="org.apache.manifoldcf.authorities.authorities.amazons3.AmazonS3Authority"/>
	<!-- Add your repository connectors here -->
	<repositoryconnector name="AmazonS3" class="org.apache.manifoldcf.crawler.connectors.amazons3.AmazonS3Connector"/>

* License
http://www.apache.org/licenses/LICENSE-2.0 	
	
