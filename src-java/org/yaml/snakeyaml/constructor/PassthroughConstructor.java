package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Implementation of Constructor that ignores YAML tags.
 *
 * This is used as a fallback strategies to use the underlying type instead of
 * throwing an exception.
 */
public class PassthroughConstructor extends Constructor {

    private class PassthroughConstruct extends AbstractConstruct {
        public Object construct(Node node) {
            // reset node to scalar tag type for parsing
            Tag tag = null;
            switch (node.getNodeId()) {
            case scalar:
                tag = Tag.STR;
            case sequence:
                tag = Tag.SEQ;
            default:
                tag = Tag.MAP;
            }

            node.setTag(tag);
            return getConstructor(node).construct(node);
        }

        public void construct2ndStep(Node node, Object object) {}
    }

    public PassthroughConstructor() {
        // Add a catch-all to catch any unidentifiable nodes
        this.yamlMultiConstructors.put("", new PassthroughConstruct());
    }
}
