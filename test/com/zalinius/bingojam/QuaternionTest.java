package com.zalinius.bingojam;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class QuaternionTest {
	public final double ERROR_MARGIN = 1E-12;
	
	@ParameterizedTest
	@ValueSource(doubles = {0, 0.5, -0.2, 1.5, Math.PI, Math.E}) // six numbers
	void rotate_vectorAroundItself_returnsItself(double angle) throws Exception {
		Vector3 vector = new Vector3(1, 0, 0);
		Quaternion quaternion = Quaternion.buildQuaternion(vector, angle);
		
		Vector3 result = quaternion.rotate(vector);
		
		assertEquals(vector.x, result.x, ERROR_MARGIN);
		assertEquals(vector.y, result.y, ERROR_MARGIN);
		assertEquals(vector.z, result.z, ERROR_MARGIN);
	}
	
	
	@Test
	void rotate_vectorAQuarterAroundPerpendicularAxis_returnsThirdAxis() throws Exception {
		Vector3 rotationAxis = new Vector3(1, 0, 0);
		Vector3 rotatee = new Vector3(0, 1, 0);
		double angle = Math.PI/2;
		Quaternion quaternion = Quaternion.buildQuaternion(rotationAxis, angle);
		
		Vector3 result = quaternion.rotate(rotatee);
		
		Vector3 expected = new Vector3(0, 0, 1);
		assertEquals(expected.x, result.x, ERROR_MARGIN);
		assertEquals(expected.y, result.y, ERROR_MARGIN);
		assertEquals(expected.z, result.z, ERROR_MARGIN);
	}

}
