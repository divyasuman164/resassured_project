package rest_assured_assignment;


import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Pojo.PojoClass;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;

public class rest_assured_assignment {
	@Test
	public void CreateuserBypostMethod(ITestContext val) throws JsonProcessingException
	{
		RestAssured.baseURI = "http://localhost:3000";
		PojoClass obj=new PojoClass();
		obj.setUsername("Divya");
		obj.setEmail("divya@gmil.com");
		obj.setPassword("1234");
		obj.setUserStatus("active");
		ObjectMapper objmapper= new ObjectMapper();
		
		Response resp = given()
			.contentType(ContentType.JSON)
			.body(objmapper.writeValueAsString(obj)).
		when()
			.post("/RestAssuredAPI").
		then()
			.statusCode(201)
			.log()
			.all()
			.extract()
			.response();
		String id=resp.jsonPath().getString("id");
		val.setAttribute("id",id);
			
		
	}
	
	@Test(dependsOnMethods ="CreateuserBypostMethod")
	public void UpdateUserByPutMethod(ITestContext val)throws JsonProcessingException
	{
		RestAssured.baseURI = "http://localhost:3000";	
		String id=val.getAttribute("id").toString();
		
		//System.out.println(id);
		JSONObject updateuser = new JSONObject();
		updateuser.put("username", "Suman");
		updateuser.put("email", "suman@gmail.com");
		updateuser.put("password", "1234");
		updateuser.put("userStatus", "active");
		 			   given()
						.put("/RestAssuredAPI/"+id).
					   then()
					   	.statusCode(200)
					   	.log()
					   	.all();
		
	}
	
	@Test(dependsOnMethods="UpdateUserByPutMethod" )
	public void login(ITestContext val)
	{
		RestAssured.baseURI="http://localhost:3000/";
		String loginId=val.getAttribute("id").toString();
		//String usernm=val.getAttribute("username").toString();
		given()
			.queryParam("id", loginId)
			.get("/RestAssuredAPI/"+loginId).
		then()
			.statusCode(200)
			.log()
			.all();
	}
	
	@Test(dependsOnMethods = "UpdateUserByPutMethod")
	public void deleteUser(ITestContext val) {
		RestAssured.baseURI="http://localhost:3000/";
		String id = val.getAttribute("id").toString();
		
		given()
			.delete("/RestAssuredAPI/"+id).
		then()
			.statusCode(200)
			.log()
			.all();
	}

}
