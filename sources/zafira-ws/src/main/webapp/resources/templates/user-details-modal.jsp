<%@ page 
    language="java"
    contentType="text/html; charset=UTF-8"
    trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/fragments/taglibs.jsp" %>

<div class="modal-header">
	<i class="fa fa-times cancel-button" aria-hidden="true" ng-click="cancel()"></i>
	<h3>User details
	</h3>
</div>
<div class="modal-body">
	<form name="userForm">
		<div class="form-group">
			<label>Username</label> 
			<input type="text" class="form-control" data-ng-model="user.userName" required></input>
		</div>
		<div class="form-group">
			<label>Email</label> 
			<input type="text" class="form-control" data-ng-model="user.email"></input>
		</div>
		<div class="form-group">
			<label>First name</label> 
			<input type="text" class="form-control" data-ng-model="user.firstName"></input>
		</div>
		<div class="form-group">
			<label>Last name</label> 
			<input type="text" class="form-control" data-ng-model="user.lastName"></input>
		</div>
		<div class="form-group">
			<label>Password</label> 
			<input type="password" class="form-control" data-ng-model="user.password"></input>
		</div>
	</form>
</div>
<div class="modal-footer">
	<button data-ng-if="user.id" class="btn btn-danger" data-ng-really-message="Do you really want to delete user?" data-ng-really-click="deleteUser(user)">Delete</button>
	<button class="btn btn-success" data-ng-click="updateUser(user)"  data-ng-disabled="userForm.$invalid">
    	Save
    </button>
</div>