<%@ page 
    language="java"
    contentType="text/html; charset=UTF-8"
    trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/fragments/taglibs.jsp" %>

<div class="modal-header">
	<i class="fa fa-times cancel-button" aria-hidden="true" ng-click="cancel()"></i>
	<h3>Dashboard settings

	</h3>
</div>
<div class="modal-body">
	<form name="dashboardForm">
		<div class="form-group">
			<label>Title</label> 
			<input type="text" class="form-control" data-ng-model="dashboard.title" required></input>
		</div>
		<div class="form-group">
			<label>Type</label>
			<select class="form-control validation" data-ng-model="dashboard.type" required data-ng-if="isNew">
				<option value="GENERAL">General</option>
				<option value="USER_PERFORMANCE">User performance</option>
			</select>
			<select class="form-control validation" data-ng-model="dashboard.type" required data-ng-if="!isNew" disabled>
				<option value="GENERAL">General</option>
				<option value="USER_PERFORMANCE">User performance</option>
			</select>
		</div>
		<div class="form-group">
			<label>Position</label> 
			<input type="number" class="form-control" data-ng-model="dashboard.position" required></input>
		</div>
		<div data-ng-if="!isNew">
			<label>Attributes</label> 
			<div class="row" style="margin-bottom: 10px;">
				<div class="col-lg-5"><input placeholder="key" type="text" class="form-control"></input></div>
				<div class="col-lg-5"><input placeholder="value" type="text" class="form-control"></input></div>
				<div class="col-lg-2">
					<button type="button" class="btn btn-default btn-circle" style="border-color: green;">
						<i class="fa fa-floppy-o" style="color: green;"></i>
                    </button>
                    <button type="button" class="btn btn-default btn-circle" style="border-color: red;">
						<i class="fa fa-trash" style="color: red;"></i>
                    </button>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-5"><input placeholder="key" type="text" class="form-control"></input></div>
				<div class="col-lg-5"><input placeholder="value" type="text" class="form-control"></input></div>
				<div class="col-lg-2">
					<button type="button" class="btn btn-default btn-circle">
						<i class="fa fa-plus"></i>
                    </button>
				</div>
			</div>
		</div>
	</form>
</div>
<div class="modal-footer">
	<button data-ng-if="dashboard.id" class="btn btn-danger" data-ng-really-message="Do you really want to delete dashboard?" data-ng-really-click="deleteDashboard(dashboard)">Delete</button>
	<button data-ng-if="isNew" class="btn btn-success" data-ng-click="createDashboard(dashboard)"  data-ng-disabled="dashboardForm.$invalid">
    	Create
    </button>
	<button data-ng-if="!isNew" class="btn btn-success" data-ng-click="updateDashboard(dashboard)"  data-ng-disabled="dashboardForm.$invalid">
    	Save
    </button>
</div>