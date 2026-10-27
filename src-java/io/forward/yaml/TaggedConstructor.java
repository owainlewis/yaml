package io.forward.yaml;

import java.util.Map;
import java.util.function.Function;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Constructor that hands values with registered local tags (for example
 * {@code !money 12.50}) to a user function.
 *
 * The tagged node is first built as its underlying YAML type (string,
 * list or map) and the result is passed to the handler. When
 * {@code passthroughUnknown} is true, tags without a handler are treated
 * as their underlying type instead of raising an error.
 */
public class TaggedConstructor extends Constructor {

    private class UnderlyingConstruct extends AbstractConstruct {
        private final Function<Object, Object> handler;

        UnderlyingConstruct(Function<Object, Object> handler) {
            this.handler = handler;
        }

        @Override
        public Object construct(Node node) {
            Tag tag;
            switch (node.getNodeId()) {
            case scalar:
                tag = Tag.STR;
                break;
            case sequence:
                tag = Tag.SEQ;
                break;
            default:
                tag = Tag.MAP;
                break;
            }
            node.setTag(tag);
            Object value = getConstructor(node).construct(node);
            return handler == null ? value : handler.apply(value);
        }
    }

    public TaggedConstructor(LoaderOptions options,
                             Map<String, Function<Object, Object>> handlers,
                             boolean passthroughUnknown) {
        super(options);
        for (Map.Entry<String, Function<Object, Object>> e : handlers.entrySet()) {
            this.yamlConstructors.put(new Tag(e.getKey()), new UnderlyingConstruct(e.getValue()));
        }
        if (passthroughUnknown) {
            this.yamlMultiConstructors.put("", new UnderlyingConstruct(null));
        }
    }
}
