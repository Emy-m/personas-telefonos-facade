package main;

import java.util.Arrays;

import facade.Facade;

public class Main {
	public static void main(String[] args) {
		Facade fakeit = new Facade();
		String sql = "select * from personas p left join telefonos t on (p.id_persona = t.id_persona)";

		System.out.println(fakeit.queryResultAsAsociation(sql));
		System.out.println(Arrays.deepToString(fakeit.queryResultAsArray(sql).toArray()));
	}
}
