/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.uruma.example.employee.form;

import java.util.List;

import org.seasar.uruma.annotation.ExportSelection;
import org.seasar.uruma.annotation.ExportValue;
import org.seasar.uruma.annotation.ImportExportValue;
import org.seasar.uruma.annotation.ImportSelection;
import org.seasar.uruma.example.employee.dto.DepartmentDto;

/**
 * 編集/新規登録画面のためのフォームクラスです。<br />
 * 
 * @author y-komori
 */
public class EmployeeEditForm {
	@ImportExportValue
	public String empno;

	@ImportExportValue
	public String ename;

	@ImportExportValue
	public String job;

	@ImportExportValue
	public String mgr;

	@ImportExportValue
	public String hiredate;

	@ImportExportValue
	public String sal;

	@ImportExportValue
	public String comm;

	@ExportValue(id = "dept")
	public List<DepartmentDto> deptList;

	@ExportSelection(id = "dept")
	@ImportSelection(id = "dept")
	public DepartmentDto selectedDepartmentDto;

	public Integer deptno;

	public int versionNo;
}
