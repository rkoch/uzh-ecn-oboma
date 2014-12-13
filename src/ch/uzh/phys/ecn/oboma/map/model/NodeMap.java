/*
 * The MIT License (MIT)
 * Copyright © 2014 different authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ch.uzh.phys.ecn.oboma.map.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.phys.ecn.oboma.map.api.INode;
import ch.uzh.phys.ecn.oboma.map.api.INodeMap;


public class NodeMap
        implements INodeMap {

    private final Map<String, INode> mNodes;


    public NodeMap() {
        mNodes = new HashMap<>();
    }


    public void add(INode pNode) {
        mNodes.put(pNode.getId(), pNode);
    }

    public void add(INode pNode, String pConnectFrom, String pConnectTo) {
        checkArgument(mNodes.containsKey(pConnectFrom));
        checkArgument(mNodes.containsKey(pConnectTo));

        mNodes.put(pNode.getId(), pNode);

        INode start = mNodes.get(pConnectFrom);
        INode end = mNodes.get(pConnectTo);

        start.addDestination(pNode);
        pNode.addOrigin(start);

        pNode.addDestination(end);
        end.addOrigin(pNode);
    }


    @Override
    public List<INode> getNodes() {
        return new ArrayList<>(mNodes.values());
    }


    @Override
    public void preelapse() {
        mNodes.values().forEach(INode::preelapse);
    }

    @Override
    public void postelapse() {
        mNodes.values().forEach(INode::postelapse);
    }

    @Override
    public void infect() {
        mNodes.values().forEach(INode::infect);
    }

}
