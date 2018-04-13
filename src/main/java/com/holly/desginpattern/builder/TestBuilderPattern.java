package com.holly.desginpattern.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestBuilderPattern {
	public static void main(String[] args) throws ParseException {
		BuildDirector command = new BuildDirector();
		CarBuilder builder = new CarEngineer();
		command.BuildCar(builder);
		CarProduct product = builder.getResult();
		product.show();
		
	}
}
