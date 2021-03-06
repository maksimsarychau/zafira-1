/*******************************************************************************
 * Copyright 2013-2018 QaProSoft (http://www.qaprosoft.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.qaprosoft.zafira.ws.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.qaprosoft.zafira.models.db.Permission;
import com.qaprosoft.zafira.models.dto.auth.JwtUserType;
import com.qaprosoft.zafira.models.dto.auth.UserGrantedAuthority;
import com.qaprosoft.zafira.models.dto.errors.Error;
import com.qaprosoft.zafira.models.dto.errors.ErrorCode;
import com.qaprosoft.zafira.models.dto.errors.ErrorResponse;
import com.qaprosoft.zafira.services.exceptions.ForbiddenOperationException;
import com.qaprosoft.zafira.services.exceptions.InvalidTestRunException;
import com.qaprosoft.zafira.services.exceptions.JobNotFoundException;
import com.qaprosoft.zafira.services.exceptions.TestNotFoundException;
import com.qaprosoft.zafira.services.exceptions.TestRunNotFoundException;
import com.qaprosoft.zafira.services.exceptions.UnableToRebuildCIJobException;
import com.qaprosoft.zafira.services.exceptions.UserNotFoundException;

public abstract class AbstractController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);
	protected static final String TEST_RUNS_WEBSOCKET_PATH = "/topic/testRuns";
	private static final String TESTS_WEBSOCKET_PATH = "/topic/testRuns/%s/tests";
	protected static final String STATISTICS_WEBSOCKET_PATH = "/topic/statistics";

	@Resource(name = "messageSource")
	protected MessageSource messageSource;

	protected String getTestsWebsocketPath(Long testRunId)
	{
		return String.format(TESTS_WEBSOCKET_PATH, testRunId);
	}
	
	protected JwtUserType getPrincipal()
	{
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user instanceof JwtUserType ? (JwtUserType) user : null;
	}
	
	protected Long getPrincipalId()
	{
		JwtUserType user = getPrincipal();
		return user != null ? user.getId() : 0;
	}
	
	protected String getPrincipalName()
	{
		UserDetails user = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return user != null ? user.getUsername() : "";
	}
	
	protected boolean isAdmin()
	{
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
	}

	protected boolean hasPermission(Permission.Name name)
	{
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.flatMap(grantedAuthority -> ((UserGrantedAuthority) grantedAuthority).getPermissions().stream())
				.anyMatch(permission -> permission.equalsIgnoreCase(name.name()));
	}

	protected boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
	}
	
	@ExceptionHandler(JobNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse handleJobNotFoundException(JobNotFoundException e)
	{
		ErrorResponse result = new ErrorResponse();
		result.setError(new Error(ErrorCode.JOB_NOT_FOUND));
		return result;
	}
	
	@ExceptionHandler(TestRunNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse handleTestRunNotFoundException(TestRunNotFoundException e)
	{
		ErrorResponse result = new ErrorResponse();
		result.setError(new Error(ErrorCode.TEST_RUN_NOT_FOUND));
		return result;
	}
	
	@ExceptionHandler(TestNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse handleTestNotFoundException(TestNotFoundException e)
	{
		ErrorResponse result = new ErrorResponse();
		result.setError(new Error(ErrorCode.TEST_RUN_NOT_FOUND));
		return result;
	}
	
	@ExceptionHandler(InvalidTestRunException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleInvalidTestRunException(InvalidTestRunException e)
	{
		ErrorResponse result = new ErrorResponse();
		result.setError(new Error(ErrorCode.INVALID_TEST_RUN));
		return result;
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e) 
	{
		LOGGER.error(e.getMessage());
    }
	
	@ExceptionHandler(UnableToRebuildCIJobException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleUnableToRebuildCIJobException(UnableToRebuildCIJobException e) 
	{
		ErrorResponse result = new ErrorResponse();
		result.setError(new Error(ErrorCode.TEST_RUN_NOT_REBUILT));
		return result;
    }
	
	@ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleBadCredentialsException(BadCredentialsException e) 
	{
		ErrorResponse result = new ErrorResponse();
		result.setError(new Error(ErrorCode.UNAUTHORIZED));
		return result;
    }
	
	@ExceptionHandler(ForbiddenOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleForbiddenOperationException(ForbiddenOperationException e) 
	{
		ErrorResponse result = new ErrorResponse();
		result.setError(new Error(ErrorCode.FORBIDDENT));
		return result;
    }
	
	@ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) 
	{
		ErrorResponse result = new ErrorResponse();
		result.setError(new Error(ErrorCode.USER_NOT_FOUND));
		return result;
    }
	
	protected void checkCurrentUserAccess(long userId) throws ForbiddenOperationException
	{
		if(!isAdmin() && userId != getPrincipalId())
		{
			throw new ForbiddenOperationException();
		}
	}
}
