#!/bin/bash

#general
HOSTNAME=`hostname`
PROJECT_NAME="automatic"
GLASSFISH_HOME="/opt/glassfish/bin/"
DATABASE_NAME="ssn_"$PROJECT_NAME 
PATH_EAR="/tmp/remote/packages/ssn3.1.ear"
SERVER_DATABASE=$HOSTNAME
USER_DATABASE="root"
PASSWORD_DATABASE="vitalnoc"
DATABASE_PORT=3306

#collector
COLLECTOR_IP="127.0.0.1"
COLLECTOR_HOSTNAME=$HOSTNAME

####################################################################
#creating database 
####################################################################

#change mysql by mysql-ib ***************
echo "creating database"
mysql -h $SERVER_DATABASE -u$USER_DATABASE -p$PASSWORD_DATABASE <<EOF
CREATE DATABASE IF NOT EXISTS $DATABASE_NAME;
USE $DATABASE_NAME;

#table categories
CREATE TABLE categories (categorie_id bigint(20) NOT NULL AUTO_INCREMENT,  categorie_active int(11) DEFAULT NULL,  categorie_name varchar(255) DEFAULT NULL,  categorie_order int(2) DEFAULT NULL,  PRIMARY KEY (categorie_id)) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

#data for table categories
truncate table categories;
INSERT INTO categories (categorie_active, categorie_name, categorie_order) VALUES ('1', 'Bandwidth', '1');
INSERT INTO categories (categorie_active, categorie_name, categorie_order) VALUES ('1', 'Protocols', '2');
INSERT INTO categories (categorie_active, categorie_name, categorie_order) VALUES ('1', 'Host', '3');
INSERT INTO categories (categorie_active, categorie_name, categorie_order) VALUES ('1', 'Conversations', '4');
INSERT INTO categories (categorie_active, categorie_name, categorie_order) VALUES ('1', 'Web', '5');
INSERT INTO categories (categorie_active, categorie_name, categorie_order) VALUES ('1', 'QoS', '6');
INSERT INTO categories (categorie_active, categorie_name, categorie_order) VALUES ('1', 'Ports', '7');

#table subcategories
CREATE TABLE subcategories (subcategorie_id BIGINT(20) NOT NULL AUTO_INCREMENT, subcategorie_active INT(11) DEFAULT NULL, subcategorie_name VARCHAR(255) DEFAULT NULL,  subcategorie_categorie_id BIGINT(20) DEFAULT NULL,  subcategorie_order INT(2) DEFAULT NULL,  PRIMARY KEY (subcategorie_id)) ENGINE=MYISAM AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;

#data for table subcategories            
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Bandwidth Over Time Bits', '1', '1');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Bandwidth Over Time Bytes', '1', '2');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Bandwidth Over Time Source Bits', '1', '3');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Bandwidth Over Time Destination Bits', '1', '4');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Bandwidth Over Time Source and Destination Bits', '1', '5');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('0', 'Bandwidth Over Time Bits Live', '1', '6');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Network Protocols', '2', '1');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'IP Protocols', '2', '2');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'TCP Protocols', '2', '3');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'UDP Protocols', '2', '4');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'IP Talkers Bytes', '3', '1');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'IP Talkers Destinations Bytes', '3', '2');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'IP Talkers Sources Bytes', '3', '3');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Hostname Talkers Bytes', '3', '4');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Hostname Talkers Destinations Bytes', '3', '5');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Hostname Talkers Sources Bytes', '3', '6');            
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Host Conversations Bytes', '4', '1');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Host TCP Conversations Bytes', '4', '2');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Host UDP Conversations Bytes', '4', '3');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Web Server Hosts', '5', '1');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Type of Service', '6', '1');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'Top Ports', '7', '1');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'TCP Ports', '7', '2');
INSERT INTO subcategories (subcategorie_active, subcategorie_name, subcategorie_categorie_id, subcategorie_order) VALUES ('1', 'UDP Ports', '7', '3');

#table collectors
CREATE TABLE collectors (collector_id bigint(20) NOT NULL AUTO_INCREMENT, collector_type int(1) DEFAULT NULL COMMENT '1 openblocks 2 server', collector_host varchar(50) DEFAULT NULL, collector_ip varchar(15)DEFAULT NULL, collector_site varchar(100) DEFAULT NULL, collector_description varchar(150) DEFAULT NULL, collector_port int(5) DEFAULT NULL, collector_port_connection_server int(5) DEFAULT NULL,   collector_is_active int(1) DEFAULT NULL COMMENT '1 active 0 no active', collector_context varchar(25) DEFAULT NULL COMMENT 'part of url', collector_date_register timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date of register', PRIMARY KEY (collector_id)) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

#data for collectors
INSERT INTO collectors (collector_type, collector_host, collector_ip, collector_is_active) VALUES ('2', '$COLLECTOR_HOSTNAME', '$COLLECTOR_IP', '1');

#table configuration            
CREATE TABLE configuration (id_configuration bigint(20) NOT NULL AUTO_INCREMENT,help_path varchar(255) DEFAULT NULL,jws_path varchar(255) DEFAULT NULL,jms_time_wait_connectivity int(11) DEFAULT NULL, jms_time_wait_message int(11) DEFAULT NULL,project_name varchar(255) DEFAULT NULL,seconds_live varchar(20) DEFAULT NULL,used_master_shaper varchar(3) DEFAULT NULL,master_shaper_server varchar(20) DEFAULT NULL, PRIMARY KEY (id_configuration)) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

#data for table configuration
truncate table configuration;
INSERT INTO configuration (jws_path, help_path, project_name, jms_time_wait_message, jms_time_wait_connectivity,seconds_live, used_master_shaper) VALUES ('http://localhost:8080/ssn-war/help','http://localhost:8080/ssn-war/help', '$PROJECT_NAME', '10000', '10000','20', 'no');

#table users
CREATE TABLE users (user_id bigint(20) NOT NULL AUTO_INCREMENT,user_name varchar(50) DEFAULT NULL,user_password varchar(50) DEFAULT NULL,user_level int(1) DEFAULT NULL COMMENT '1 =admin 2 = guest',  user_description varchar(100) DEFAULT NULL, PRIMARY KEY (user_id)) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

#data for table users
insert  into users(user_id,user_name,user_password,user_level,user_description) values (1,'skuarch','123',1,'administrator');
insert  into users(user_id,user_name,user_password,user_level,user_description) values (2,'julio','123',1,'administrator');
insert  into users(user_id,user_name,user_password,user_level,user_description) values (3,'invitado','123',2,'guest');
EOF

echo "database was created successfully"

####################################################################
#glassfish configuration
####################################################################
cd $GLASSFISH_HOME
./asadmin set server-config.jms-service.jms-host.default_JMS_host.host="$COLLECTOR_HOSTNAME"
./asadmin set server.iiop-service.iiop-listener.orb-listener-1.address="$COLLECTOR_HOSTNAME"


####################################################################
#deploying .ear in glassfish
####################################################################
./asadmin deploy $PATH_EAR

./asadmin create-jdbc-connection-pool --user admin --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource --restype javax.sql.DataSource --property user=$USER_DATABASE:password=$PASSWORD_DATABASE:DatabaseName=$DATABASE_NAME:ServerName=$SERVER_DATABASE:port=$DATABASE_PORT connection_pool_ssn_3_1

./asadmin create-jdbc-resource --user admin --connectionpoolid connection_pool_ssn_3_1 jdbc/ssn_3_1

./asadmin create-jms-resource --restype=javax.jms.TopicConnectionFactory jms_connection_factory_ssn_3_1

./asadmin create-jms-resource --restype=javax.jms.Topic jms_topic_ssn_3_1

#restart glassfish
./asadmin stop-domain
./asadmin start-domain

