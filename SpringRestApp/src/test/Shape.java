import com.google.gson.Gson;

public abstract class Shape {
	protected int x;
	protected int y;
}
class Circle extends Shape {
	int radius;
	
	@Override
	public String toString() {
		return "radius:"+radius;
	}
}
class Rectangle extends Shape {
	int width;
	int height;
	
	@Override
	public String toString() {
		return "width :"+width+" height :"+height;
	}
}
class Diamond extends Shape {
	int width;
	int height;
	
	@Override
	public String toString() {
		return "width :"+width+" height:"+height;
	}
}
class Drawing {
	Shape bottomShape;
	Shape topShape;
	
	@Override
	public String toString() {
		return "bottomShape:"+bottomShape" topShape:"+topShape;
	}
}
public class Test {
	public static void main(String[] args) {
		/*RuntimeTypeAdapter<Shape> shapeAdapter = RuntimeTypeAdapter.of(Shape.class, "type");
		shapeAdapter.registerSubtype(Rectangle.class, "Rectangle");
		shapeAdapter.registerSubtype(Circle.class, "Circle");
		shapeAdapter.registerSubtype(Diamond.class, "Diamond");*/


		RuntimeTypeAdapter<Shape> shapeAdapter = RuntimeTypeAdapterFactory.of(Shape.class)
				.registerSubtype(Rectangle.class)
				.registerSubtype(Circle.class)
				.registerSubtype(Diamond.class);
		
		Gson gson = new GsonBuilder()
		.registerTypeAdapter(Shape.class, shapeAdapter)
		.create();

		
		Shape diamond = new Diamond();
		Shape rectangle = new Rectangle();
		
		System.out.println("diamond : "+gson.toJson(diamond));
		System.out.println("rectangle : "+gson.toJson(rectangle));
		
		
		Shape deseDiamond = gson.fromJson(gson.toJson(diamond), Shape.class);
		Shape deseRectangle = gson.fromJson(gson.toJson(diamond), Shape.class);
		
		if(deseDiamond instanceof Diamond) {
			System.out.println("deseDiamond instanceof Diamond");
		}
		if(deseRectangle instanceof Rectangle) {
			System.out.println("deseRectangle instanceof Rectangle");
		}
	}
}
