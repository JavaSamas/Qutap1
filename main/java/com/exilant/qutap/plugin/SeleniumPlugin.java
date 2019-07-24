package com.exilant.qutap.plugin;

import org.json.JSONObject;

import com.exilant.qutap.agent.Task;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SeleniumPlugin implements Task {

	@Override
	public JSONObject processRequest(JSONObject requestJSON) {
		System.out.println("processRequest(JSONObject requestJSON) 22222called");

		JSONObject respJSON = getJson(requestJSON);
		System.out.println(respJSON);
		return respJSON;

	}

	String error = "";
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	JSONObject respJSON = new JSONObject();
	JSONObject mainJSON = new JSONObject();

	StringWriter errors = new StringWriter();

	List<JSONObject> ParamList = new ArrayList<JSONObject>();

	public JSONObject getJson(JSONObject requestJSON) {
		JSONObject resJSONFromAction = new JSONObject();
		List<Object> myParamList = new ArrayList<Object>();

		JSONArray jsonMainArr = requestJSON.getJSONArray("testStepList");
		System.out.println(jsonMainArr.toString());

		System.out.println(jsonMainArr.toString());

		for (int i = 0; i < jsonMainArr.length(); i++) {
			JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
			String action = childJSONObject.getString("action");
			String paramGroupObject = childJSONObject.getString("paramGroupObject");
			String stepParam= childJSONObject.getString("stepParam");
			System.out.println(stepParam+"StepParam");
			String locator = childJSONObject.getString("paramGroupId");
		//	String comment = childJSONObject.getString("comment");
			JSONArray value = childJSONObject.getJSONArray("testParamData");

			if (!(paramGroupObject.equals("NoObject"))) {
				
				myParamList.add(paramGroupObject);
				System.out.println(paramGroupObject);
			}
			if (!(locator.equals(""))) {
				myParamList.add(locator);
				System.out.println(locator);
			} 
			if (!(stepParam.equals(""))) {
				
				
				int valuintValuee=(int)Double.parseDouble(stepParam.trim());
				
				myParamList.add(new Integer(valuintValuee));
		} 
			for (int j = 0; j < value.length(); j++) {
				if (!(value.getString(j).equals(""))) {
					myParamList.add(value.getString(j));
				}

			}

			System.out.println(action);
			myParamList.stream().forEach(System.out::println);
			Object[] paramListObject = new String[myParamList.size()];
			paramListObject = myParamList.toArray();
			Date startDate = new Date();
           //resJSONFromAction = runReflectionMethod("com.exilant.itap.plugin.SeleniumPluginHelper", action, paramListObject);
			resJSONFromAction = runReflectionMethod("com.exilant.qutap.plugin.SeleniumPluginHelper", action, paramListObject);
			Date endDate = new Date();
	//		resJSONFromAction.put("testStepId", childJSONObject.get("testStepId").toString());
			resJSONFromAction.put("StartDateTime", dateFormat.format(startDate));
			resJSONFromAction.put("EndDateTime", dateFormat.format(endDate));
			resJSONFromAction.put("testStepId",childJSONObject.getString("testStepsId"));
			resJSONFromAction.put("runnerType", requestJSON.getString("runnerType"));
			resJSONFromAction.put("paramGroupObject", paramGroupObject);
			resJSONFromAction.put("action", action);
			resJSONFromAction.put("testParamData", value);
		//	resJSONFromAction.put("comment", comment);
			ParamList.add(resJSONFromAction);

			myParamList.clear();

		}

		mainJSON.put("TestResult", ParamList);
		System.out.println(mainJSON);

		return mainJSON;

	}

	public JSONObject runReflectionMethod(String strClassName, String strMethodName, Object... inputArgs) {
		System.out.println("runReflectionMethod(String strClassName, String strMethodName, Object... inputArgs) called");
		Class<?> params[] = new Class[inputArgs.length];
		Object ob = null;
		String res = "";

		for (int i = 0; i < inputArgs.length; i++) {
			if (inputArgs[i] instanceof String) {
				params[i] = String.class;
			}
			if (inputArgs[i] instanceof Integer) {
				params[i] = Integer.class;
			}
		}
		try {
			Class<?> cls = Class.forName(strClassName);
			Object _instance = cls.newInstance();
			Method myMethod = cls.getDeclaredMethod(strMethodName, params);
			ob = myMethod.invoke(_instance, inputArgs);

		} catch (ClassNotFoundException e) {
			System.err.format(strClassName + ":- Class not found%n");
			e.printStackTrace(new PrintWriter(errors));
			// error = errors.toString();
			error = e.getMessage();

		} catch (IllegalArgumentException e) {
			System.err.format("Method invoked with wrong number of arguments%n");
			e.printStackTrace(new PrintWriter(errors));

			error = e.getMessage();

		} catch (NoSuchMethodException e) {

			e.printStackTrace(new PrintWriter(errors));
			// error = errors.toString();
			error = e.getMessage();

		} catch (InvocationTargetException e) {
			System.err.format("Exception thrown by an invoked method%n");
			e.printStackTrace();
			// e.printStackTrace(new PrintWriter(errors));
			// error = errors.toString();
			error = e.getMessage();

		} catch (IllegalAccessException e) {
			System.err.format("Can not access a member of class with modifiers private%n");
			e.printStackTrace();
			e.printStackTrace(new PrintWriter(errors));
			// error = errors.toString();
			error = e.getMessage();

		} catch (InstantiationException e) {
			System.err.format("Object cannot be instantiated for the specified class using the newInstance method%n");
			e.printStackTrace(new PrintWriter(errors));
			// error = errors.toString();
			error = e.getMessage();

		} catch (Exception e) {
			e.printStackTrace(new PrintWriter(errors));
			// error = errors.toString();
			error = e.getMessage();
		}

		if (ob == null) {
			respJSON.put("status", "FAIL");
			respJSON.put("response", "");
			respJSON.put("error", error);
			return respJSON;
		} else {
			return (JSONObject) ob;
		}

	}

}