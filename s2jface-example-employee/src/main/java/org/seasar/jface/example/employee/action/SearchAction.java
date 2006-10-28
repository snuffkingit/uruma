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

import org.eclipse.swt.widgets.Shell;
import org.seasar.jface.annotation.EventListener;
import org.seasar.jface.annotation.ExportValue;
import org.seasar.jface.annotation.ImportValue;
import org.seasar.jface.annotation.InitializeMethod;
import org.seasar.jface.annotation.ReturnValue;
import org.seasar.jface.example.employee.dxo.SearchActionDxo;

import examples.jsf.dto.DepartmentDto;
import examples.jsf.dto.EmployeeSearchDto;
import examples.jsf.logic.EmployeeLogic;

public class SearchAction {

    private EmployeeLogic employeeLogic;

    private SearchActionDxo dxo;

    private Shell shell;

    @ImportValue
    private String empno;

    @ImportValue
    private String ename;

    @ImportValue
    private String job;

    @ImportValue
    private String mgr;

    @ImportValue
    private String fromHiredate;

    @ImportValue
    private String toHiredate;

    @ImportValue
    private String fromSal;

    @ImportValue
    private String toSal;

    @ImportValue(id = "dept")
    private String deptName;

    @ExportValue(id = "dept", label = "dname")
    private List<DepartmentDto> deptList;

    private Integer deptno;

    @ReturnValue
    private List searchResult = null;

    @InitializeMethod
    public void initialize() {
        deptList = employeeLogic.getAllDepartments();
    }

    @EventListener(id = "ok")
    public void onOk() {
        bindDept();

        EmployeeSearchDto searchDto = dxo.convert(this);
        searchResult = employeeLogic.searchEmployeeDtoList(searchDto);
        shell.close();
    }

    @EventListener(id = "cancel")
    public void onCancel() {
        searchResult = null;
        shell.close();
    }

    private void bindDept() {
        if (deptList == null) {
            return;
        }
        for (DepartmentDto dto : deptList) {
            if (dto.getDname().equals(deptName)) {
                deptno = dto.getDeptno();
                break;
            }
        }
    }

    public void setEmployeeLogic(EmployeeLogic employeeLogic) {
        this.employeeLogic = employeeLogic;
    }

    public void setDxo(SearchActionDxo dxo) {
        this.dxo = dxo;
    }

    public String getEmpno() {
        return this.empno;
    }

    public String getEname() {
        return this.ename;
    }

    public String getFromHiredate() {
        return this.fromHiredate;
    }

    public String getFromSal() {
        return this.fromSal;
    }

    public String getJob() {
        return this.job;
    }

    public String getMgr() {
        return this.mgr;
    }

    public String getToHiredate() {
        return this.toHiredate;
    }

    public String getToSal() {
        return this.toSal;
    }

    public Integer getDeptno() {
        return this.deptno;
    }

}
