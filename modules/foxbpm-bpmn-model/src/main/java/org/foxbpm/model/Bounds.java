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
package org.foxbpm.model;

/**
 * 常量类
 * 
 * @author yangguangftlp
 * @date 2014年10月20日
 */
public class Bounds {
	protected float x;
	protected float y;
	protected float height;
	protected float width;
	protected String bpmnElement;
	protected boolean isMarkerVisible;
	protected boolean isHorizontal;
	protected boolean isExpanded;
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public String getBpmnElement() {
		return bpmnElement;
	}
	public void setBpmnElement(String bpmnElement) {
		this.bpmnElement = bpmnElement;
	}
	public boolean isMarkerVisible() {
		return isMarkerVisible;
	}
	public void setMarkerVisible(boolean isMarkerVisible) {
		this.isMarkerVisible = isMarkerVisible;
	}
	
	public void setHorizontal(boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
	}
	
	public boolean isHorizontal() {
		return isHorizontal;
	}
	
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
	
	public boolean isExpanded() {
		return isExpanded;
	}
}
