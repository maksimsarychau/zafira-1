<%@ page 
    language="java"
    contentType="text/html; charset=UTF-8"
    trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/fragments/taglibs.jsp" %>

<div class="modal-header">
	<h3>
		{{title}}
	</h3>
</div>
<div class="modal-body">
	<div class="row">
		<div class="col-lg-12">
			<form name="commentForm" novalidate>
				<div class="form-group">
					<label>Comments</label> 
					<textarea name="comment" rows=10 class="form-control validation" data-ng-model="testRun.comments"></textarea>
				</div>
			</form>
		</div>
	</div>
</div>
<div class="modal-footer">
	<button class="btn btn-success" data-ng-click="addComment()" data-ng-disabled="commentForm.$invalid">
    	Save
    </button>
     <button class="btn btn-primary" data-ng-click="cancel()">
    	Cancel
    </button>
</div>