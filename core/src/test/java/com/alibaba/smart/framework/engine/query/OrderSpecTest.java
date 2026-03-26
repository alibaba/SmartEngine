package com.alibaba.smart.framework.engine.query;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for OrderSpec.
 *
 * @author SmartEngine Team
 */
public class OrderSpecTest {

    @Test
    public void testOrderSpecCreation() {
        OrderSpec spec = new OrderSpec("gmtCreate", "gmt_create", OrderSpec.Direction.ASC);

        Assert.assertEquals("gmtCreate", spec.getPropertyName());
        Assert.assertEquals("gmt_create", spec.getColumnName());
        Assert.assertEquals(OrderSpec.Direction.ASC, spec.getDirection());
    }

    @Test
    public void testToColumnName() {
        OrderSpec spec = new OrderSpec("priority", "priority", OrderSpec.Direction.DESC);

        Assert.assertEquals("priority", spec.toColumnName());
    }

    @Test
    public void testDirectionSql() {
        OrderSpec ascSpec = new OrderSpec("id", "id", OrderSpec.Direction.ASC);
        OrderSpec descSpec = new OrderSpec("id", "id", OrderSpec.Direction.DESC);

        Assert.assertEquals("ASC", ascSpec.getDirectionSql());
        Assert.assertEquals("DESC", descSpec.getDirectionSql());
    }

    @Test
    public void testToString() {
        OrderSpec spec = new OrderSpec("claimTime", "claim_time", OrderSpec.Direction.DESC);

        Assert.assertEquals("claim_time DESC", spec.toString());
    }

    @Test
    public void testDirectionEnum() {
        Assert.assertEquals("ASC", OrderSpec.Direction.ASC.getSql());
        Assert.assertEquals("DESC", OrderSpec.Direction.DESC.getSql());
    }
}
