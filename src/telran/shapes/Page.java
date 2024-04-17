package telran.shapes;

import java.util.Iterator;
import java.util.NoSuchElementException;

import telran.shapes.exceptions.NoCanvasException;
import telran.shapes.exceptions.ShapeAlreadyExistsException;
import telran.shapes.exceptions.ShapeNotFoundException;
import telran.util.*;

public class Page implements Iterable<Shape>{
	private Shape[] shapes = new Shape[0];
	
	public void addShape(Shape shape) {
		if (Arrays.indexOf(shapes, shape) > -1) {
			throw new ShapeAlreadyExistsException(shape.getId());
		}
		shapes = Arrays.add(shapes, shape);
	}
	
	public void addShape(Long[] canvasIds, Shape shape) {
		Canvas canvas = getCanvas(canvasIds);
		canvas.addShape(shape);
	}
	
	// for tests only
	public Canvas getCanvasTest(Long[] canvasIds) {
		return getCanvas(canvasIds);
	}
	
	private Canvas getCanvas(Long[] canvasIds) {
		Canvas canvas = getCanvasById(shapes, canvasIds[0]);
		for(int i = 1; i < canvasIds.length; i++) {
			canvas = getCanvasById(canvas.shapes, canvasIds[i]);
		}
		return canvas;
	}
	
	private Canvas getCanvasById(Shape[] shapes, Long id) {
		// this CW code below doesn't allow to throw NoCanvasException 
		// because if shape is found, it always Canvas
//		int index = Arrays.indexOf(shapes, new Canvas(id));
//		if(index < 0) {
//			throw new ShapeNotFoundException(id);
//		}
//		Shape shape = shapes[index];
		Shape shape = getShape(shapes, id);
		if (shape == null) {
			throw new ShapeNotFoundException(id);
		}
		Canvas result = null;

		if (shape instanceof Canvas) {
			result = (Canvas)shape;
		} else {
			throw new NoCanvasException(id);
		}
		return result;
	}
	
	private Shape getShape(Shape[] shapes, Long id) {
		int index = 0;
		Shape foundShape = null;
		while(index < shapes.length && foundShape == null) {
    		if (shapes[index].getId() == id) {
				foundShape = shapes[index];
			}	
    		index++;
		}
		return foundShape;
	}
	
	public Shape removeShape(long id) {		
		Shape[] removedShapes = Arrays.search(shapes, s -> s.getId() == id);
		Shape removedShape = null;
		if (removedShapes.length != 0) {
			shapes = Arrays.removeIf(shapes, s -> s.getId() == id);
			removedShape = removedShapes[0];
		}
		
		return removedShape;
	}
	
	public Shape removeShape(Long[] canvasIds, long id) {
		Canvas canvas = getCanvas(canvasIds);
		Shape removedShape = canvas.removeShape(id);
		return removedShape;
	}
	
	@Override
	public Iterator<Shape> iterator() {
		
		return new PageIterator();
	}
	
	private class PageIterator implements Iterator<Shape> {
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
