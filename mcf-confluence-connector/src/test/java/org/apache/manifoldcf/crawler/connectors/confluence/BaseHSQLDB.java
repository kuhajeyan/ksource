package org.apache.manifoldcf.crawler.connectors.confluence;

public class BaseHSQLDB extends org.apache.manifoldcf.crawler.tests.ConnectorBaseHSQLDB
{
  
  protected String[] getConnectorNames()
  {
    return new String[]{"Confluence"};
  }
  
  protected String[] getConnectorClasses()
  {
    return new String[]{"org.apache.manifoldcf.crawler.connectors.confluence.ConfluenceRepositoryConnector"};
  }

}
