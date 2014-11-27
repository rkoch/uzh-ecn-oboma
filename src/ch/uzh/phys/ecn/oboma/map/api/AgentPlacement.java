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
package ch.uzh.phys.ecn.oboma.map.api;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import lombok.EqualsAndHashCode;
import ch.uzh.phys.ecn.oboma.agents.model.Agent;


@EqualsAndHashCode
class AgentPlacement
        implements ITimeDependant {

    private final Agent mAgent;
    private int         mRemainingTime;


    public AgentPlacement(Agent pAgent, int pTimeToSpend) {
        checkNotNull(pAgent);
        checkArgument(pTimeToSpend > 0);
        mAgent = pAgent;
        mRemainingTime = pTimeToSpend;
    }


    public Agent getAgent() {
        return mAgent;
    }

    public int getRemainingTime() {
        return mRemainingTime;
    }

    public boolean isStaying() {
        return mRemainingTime > 0;
    }

    public boolean decrease() {
        return --mRemainingTime > 0;
    }


    @Override
    public String toString() {
        return "AgentPlacement [agent=" + mAgent.getId() + ", remainingTime=" + mRemainingTime + "]";
    }


    @Override
    public void preelapse() {
        decrease();
    }

    @Override
    public void postelapse() {
        // Nothing to do
    }

}
