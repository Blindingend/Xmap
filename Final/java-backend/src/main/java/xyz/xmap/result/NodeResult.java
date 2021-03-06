package xyz.xmap.result;

import xyz.xmap.domain.Node;

import java.util.Collection;

public class NodeResult extends Result {
    private Collection<Node> Nodes;

    private Node node;

    public Collection<Node> getNodes() {
        return Nodes;
    }

    public void setNodes(Collection<Node> nodes) {
        Nodes = nodes;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
