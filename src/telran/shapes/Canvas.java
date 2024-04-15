package telran.shapes;

import java.util.Iterator;
import java.util.NoSuchElementException;

import telran.util.Arrays;

public class Canvas extends Shape implements Iterable<Shape>{
	private Shape[] shapes = new Shape[0];
	public Canvas(long id) {
		super(id);
		
	}
	@Override
	public int square() {
		int result = 0;
		for(Shape shape: shapes) {
			result += shape.square();
		}
		return result;
	}

	@Override
	public int perimeter() {
		int result = 0;
		for(Shape shape: shapes) {
			result += shape.perimeter();
		}
		return result;
	}
	public void addShape(Shape shape) {
		shapes = Arrays.add(shapes, shape);
	}
	public void removeShape(long id) {
		shapes = Arrays.removeIf(shapes, s -> s.getId() == id);
	}
	@Override
	public Iterator<Shape> iterator() {
		return new CanvasIterator();
	}
	private class CanvasIterator implements Iterator<Shape> {
		int currentIndex = 0;
		@Override
		public boolean hasNext() {
			
			return currentIndex < shapes.length;
		}

		@Override
		public Shape next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			return shapes[currentIndex++];
		}
		
	}

}
