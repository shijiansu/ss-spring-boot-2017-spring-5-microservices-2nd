package _2017.spring._5.microservice._2nd.ch3.springboot;

import javax.annotation.PostConstruct;
import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class EmbeddedAmqpBroker {
  Broker broker;
  BrokerOptions brokerOptions;

  public EmbeddedAmqpBroker(QpidProperties qpidProperties) {
    broker = new Broker();
    brokerOptions = new BrokerOptions();
    // option values set to "test-initial-config.json"
    brokerOptions.setConfigProperty(
        "qpid.broker.defaultPreferenceStoreAttributes", "{\"type\": \"Noop\"}");
    brokerOptions.setConfigProperty("qpid.amqp_port", qpidProperties.getQpidPort());
    brokerOptions.setConfigProperty("qpid.vhost", qpidProperties.getQpidVirtualHost());
    brokerOptions.setConfigProperty("qpid.username", qpidProperties.getQpidUserName());
    brokerOptions.setConfigProperty("qpid.password", qpidProperties.getQpidPassword());
    brokerOptions.setConfigurationStoreType("Memory");
    brokerOptions.setStartupLoggedToSystemOut(false);
  }

  @PostConstruct
  public void postConstruct() throws Exception {
    broker.startup(brokerOptions);
  }
}
