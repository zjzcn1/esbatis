package com.github.esbatis.parser;

import com.github.esbatis.mapper.MapperFactory;
import com.github.esbatis.utils.XmlNodeUtils;
import org.w3c.dom.Node;

import java.io.InputStream;
import java.util.List;

/**
 * @author jinzhong.zhang
 */
public class XMLMapperParser {

    private MapperFactory mapperFactory;
    private XPathParser parser;
    private String namespace;

    public XMLMapperParser(InputStream inputStream, MapperFactory configuration) {
        this(configuration, new XPathParser(inputStream));
    }

    public XMLMapperParser(MapperFactory mapperFactory, XPathParser parser) {
        this.mapperFactory = mapperFactory;
        this.parser = parser;
    }

    public void parse() {
        parseMapperElement(parser.evalNode("/mapper"));
    }

    private void parseMapperElement(Node mapperNode) {
        try {
            String namespace = XmlNodeUtils.getStringAttribute(mapperNode, "namespace");
            if (namespace == null || namespace.equals("")) {
                throw new ParserException("Mapper's namespace cannot be empty");
            }
            this.namespace = namespace;
            buildStatementFromContext(parser.evalNodes(mapperNode,
                    "index|update|delete|get|search|delete_by_query|update_by_query|mget|bulk"));
        } catch (Exception e) {
            throw new ParserException("Error parsing Mapper XML. Cause: " + e, e);
        }
    }

    private void buildStatementFromContext(List<Node> list) {
        for (Node context : list) {
            XMLStatementParser statementParser = new XMLStatementParser(mapperFactory, namespace, context);
            statementParser.parseStatementNode();
        }
    }

}
