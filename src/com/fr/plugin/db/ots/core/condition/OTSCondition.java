package com.fr.plugin.db.ots.core.condition;

import com.aliyun.openservices.ots.model.condition.ColumnCondition;
import com.aliyun.openservices.ots.model.condition.CompositeCondition;
import com.fr.script.Calculator;
import com.fr.stable.xml.XMLable;

/**
 * Created by richie on 16/9/5.
 */
public interface OTSCondition extends XMLable {

    String XML_TAG = "OTSCondition";

    ColumnCondition createColumnCondition(Calculator c);

    CompositeCondition.LogicOperator getLogicOperator();

    int getConditionCount();
}
