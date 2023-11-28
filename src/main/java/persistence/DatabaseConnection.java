package persistence;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {

	private static MongoClient mongoClient;

	static {
		String connectionString = "mongodb://localhost:27017";
		MongoClientURI uri = new MongoClientURI(connectionString);
		mongoClient = new MongoClient(uri);
	}

	public static MongoDatabase getDatabase(String databaseName) {
		return mongoClient.getDatabase(databaseName);
	}

	public static void closeConnection() {
		mongoClient.close();
	}
}
