module OODFinalProject_AmitMaudi_NoamLevy {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.net.http;
	requires json.simple;
	requires javafx.fxml;
	requires javafx.base;
	requires java.sql;
	requires jasypt;
	requires java.desktop;
	requires javafx.swing;
	
	opens controller to javafx.fxml;
	opens view to javafx.fxml;
	opens application to javafx.graphics, javafx.fxml;
}
