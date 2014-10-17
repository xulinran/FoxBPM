/**
 * Copyright 1996-2014 FoxBPM ORG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author yangguangftlp
 */
package org.foxbpm.bpmn.converter.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Element;
import org.foxbpm.bpmn.constants.BpmnXMLConstants;
import org.foxbpm.bpmn.converter.BaseElementXMLConverter;
import org.foxbpm.bpmn.converter.BpmnXMLConverter;
import org.foxbpm.bpmn.converter.util.BpmnXMLUtil;
import org.foxbpm.model.BpmnModel;
import org.foxbpm.model.DataVariable;
import org.foxbpm.model.FlowElement;
import org.foxbpm.model.PotentialStarter;
import org.foxbpm.model.Process;
import org.foxbpm.model.SequenceFlow;
;

/**
 * 
 * 常量类
 * 
 * @author yangguangftlp
 * @date 2014年10月15日
 */
public class ProcessParser extends BpmnParser {
	@SuppressWarnings("rawtypes")
	@Override
	public void parse(Element element, BpmnModel model) throws Exception {
		Process process = new Process();
		process.setId(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_ID));
		process.setName(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_NAME));
		process.setCategory(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_CATEGORY));
		process.setSubject(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_SUBJECT));
		process.setKey(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_KEY));
		// 扩展元素
		Element elem = null;
		String name = null;
		for (Iterator iterator = element.elements().iterator(); iterator.hasNext();) {
			elem = (Element) iterator.next();
			name = elem.getName();
			if (BpmnXMLConstants.ELEMENT_EXTENSION_ELEMENTS.equalsIgnoreCase(name)) {
				parseExtElements(process, elem);
			}
			if (BpmnXMLConstants.ELEMENT_DOCUMENTATION.equalsIgnoreCase(name)) {
				process.setDocumentation(elem.getText());
			} else if (BpmnXMLConstants.ELEMENT_SEQUENCEFLOW.equalsIgnoreCase(name)) {
				if (null == process.getSequenceFlows()) {
					process.setSequenceFlows(new HashMap<String, SequenceFlow>());
				}
				// 线条处理
				process.getSequenceFlows().put(elem.attributeValue(BpmnXMLConstants.ATTRIBUTE_ID), BpmnXMLUtil.parseSequenceFlow(elem));
			} else if (null != BpmnXMLConverter.getConverter(name)) {
				BaseElementXMLConverter converter = BpmnXMLConverter.getConverter(name);
				FlowElement flowElement = converter.cretateFlowElement();
				converter.convertXMLToModel(element, flowElement);
				process.getFlowElements().add(flowElement);
			}
		}
		model.getProcesses().add(process);
	}
	/**
	 * 处理扩展节点
	 * 
	 * @param process
	 *            当前流程
	 * @param element
	 *            流程下扩展元素
	 */
	@SuppressWarnings("rawtypes")
	private void parseExtElements(Process process, Element element) {
		String parentNodeName = element.getName();
		String nodeName = null;
		String expression = null;
		String documentation = null;
		Element elem = null;
		
		// 处理子节点
		for (Iterator iterator = element.elements().iterator(); iterator.hasNext();) {
			elem = (Element) iterator.next();
			nodeName = elem.getName();
			if (BpmnXMLConstants.ELEMENT_FORMURI.equalsIgnoreCase(nodeName)
			        || BpmnXMLConstants.ELEMENT_FORMURIVIEW.equalsIgnoreCase(nodeName)
			        || BpmnXMLConstants.ELEMENT_DATAVARIABLE.equalsIgnoreCase(nodeName)
			        || BpmnXMLConstants.ELEMENT_TASKSUBJECT.equalsIgnoreCase(nodeName)
			        || BpmnXMLConstants.ELEMENT_POTENTIALSTARTER.equals(nodeName)) {
				parseExtElements(process, elem);
				// 继续下一个
				continue;
			}
			if (BpmnXMLConstants.ELEMENT_EXPRESSION.equalsIgnoreCase(nodeName)) {
				// 表达式解析
				expression = BpmnXMLUtil.parseExpression(elem);
			} else if (BpmnXMLConstants.ELEMENT_DOCUMENTATION.equalsIgnoreCase(nodeName)) {
				documentation = elem.getText();
			}
			/** 处理表单url */
			if (BpmnXMLConstants.ELEMENT_FORMURI.equalsIgnoreCase(parentNodeName)) {
				process.setFormUri(expression);
			} else /** 处理表单视图 */
			if (BpmnXMLConstants.ELEMENT_FORMURIVIEW.equalsIgnoreCase(parentNodeName)) {
				process.setFormUriView(expression);
			} else /** 处理任务主题 */
			if (BpmnXMLConstants.ELEMENT_TASKSUBJECT.equalsIgnoreCase(parentNodeName)) {
				process.setSubject(expression);
			}
		}
		
		// 处理数据变量
		if (BpmnXMLConstants.ELEMENT_DATAVARIABLE.equalsIgnoreCase(parentNodeName)) {
			DataVariable dataVariable = new DataVariable();
			dataVariable.setId(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_ID));
			dataVariable.setBizType(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_BIZTYPE));
			dataVariable.setDataType(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_DATATYPE));
			dataVariable.setFieldName(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_FIELDNAME));
			dataVariable.setExpression(expression);
			dataVariable.setDocumentation(documentation);
			if (null == process.getDataVariables()) {
				process.setDataVariables(new ArrayList<DataVariable>());
			}
			process.getDataVariables().add(dataVariable);
		}
		// 处理启动人
		if (BpmnXMLConstants.ELEMENT_POTENTIALSTARTER.equalsIgnoreCase(parentNodeName)) {
			PotentialStarter potentialStarter = new PotentialStarter();
			potentialStarter.setId(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_ID));
			potentialStarter.setResourceType(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_RESOURCETYPE));
			potentialStarter.setDocumentation(element.attributeValue(BpmnXMLConstants.ATTRIBUTE_DESCRIPTION));
			if (null == process.getPotentialStarters()) {
				process.setPotentialStarters(new ArrayList<PotentialStarter>());
			}
			process.getPotentialStarters().add(potentialStarter);
		}
		// 处理连接器
		if (BpmnXMLConstants.ELEMENT_CONNECTORINSTANCE_ELEMENTS.equalsIgnoreCase(parentNodeName)) {
			
		}
	}
}