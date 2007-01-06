/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.jface.example.employee.action;

import java.util.List;

import javax.annotation.Resource;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.seasar.jface.S2JFaceWindowManager;
import org.seasar.jface.annotation.EventListener;
import org.seasar.jface.annotation.ExportValue;
import org.seasar.jface.annotation.Form;
import org.seasar.jface.annotation.ImportSelection;
import org.seasar.jface.annotation.InitializeMethod;
import org.seasar.jface.example.employee.dto.EmployeeDto;
import org.seasar.jface.example.employee.dto.EmployeeSearchDto;
import org.seasar.jface.example.employee.entity.Employee;
import org.seasar.jface.example.employee.logic.EmployeeLogic;

/**
 * @author bskuroneko
 */
@Form(MainAction.class)
public class MainAction {
	@Resource
	private S2JFaceWindowManager windowManager;

	@Resource
	private EmployeeLogic employeeLogic;

	private Shell shell;

	private Table employeeTable;

	@ExportValue(id = "employeeTable")
	private List<EmployeeDto> employees;

	@ImportSelection(id = "employeeTable")
	private List<EmployeeDto> selectedEmployees;

	@InitializeMethod
	public void initialize() {
		employees = employeeLogic
				.searchEmployeeDtoList(new EmployeeSearchDto());
	}

	@EventListener(id = { "menuSearch", "toolSearch" })
	public void searchEmployee() {
		List<EmployeeDto> result = (List<EmployeeDto>) windowManager
				.openModal("org/seasar/jface/example/employee/search.xml");
		if (result != null) {
			employees = result;
		}
	}

	@EventListener(id = { "menuRegist", "toolRegist" })
	public void registEmployee() {
		EmployeeDto result = (EmployeeDto) windowManager
				.openModal("org/seasar/jface/example/employee/regist.xml");
		if (result != null) {
			employees.add(result);
		}
	}

	@EventListener(id = { "menuDelete", "toolDelete" })
	public void deleteEmployee() {
		boolean result = MessageDialog.openConfirm(shell, "削除確認",
				"選択された従業員情報を削除しますか？");
		if (result) {
			try {
				for (EmployeeDto dto : selectedEmployees) {
					Employee employee = employeeLogic.getEmployee(dto
							.getEmpno());
					employeeLogic.delete(employee);
				}
			} finally {
				initialize();
			}
		}
	}

	@EventListener(id = { "menuEdit", "toolEdit" })
	public void editEmployee() {
		int selection = employeeTable.getSelectionIndex();
		EmployeeDto employee = employees.get(selection);
		EmployeeDto edited = (EmployeeDto) windowManager.openModal(
				"org/seasar/jface/example/employee/edit.xml", employee);
		if (edited != null) {
			employees.set(selection, edited);
		}
	}

	@EventListener(id = { "menuInquire", "toolInquire" })
	public void inquireEmployee() {
		int selection = employeeTable.getSelectionIndex();
		EmployeeDto employee = employees.get(selection);
		windowManager.openModal(
				"org/seasar/jface/example/employee/inquire.xml", employee);
	}

	@EventListener(id = "menuAbout")
	public void showAbout() {
		windowManager.openModal("org/seasar/jface/example/employee/about.xml");
	}

	@EventListener(id = "menuExit")
	public void exit() {
		shell.close();
	}
}
