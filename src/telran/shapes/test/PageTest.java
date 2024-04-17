package telran.shapes.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.shapes.Canvas;
import telran.shapes.Page;
import telran.shapes.Rectangle;
import telran.shapes.Shape;
import telran.shapes.Square;
import telran.shapes.exceptions.NoCanvasException;
import telran.shapes.exceptions.ShapeAlreadyExistsException;
import telran.shapes.exceptions.ShapeNotFoundException;

class PageTest {
	private static final long ID11 = 11;
	private static final int WIDTH1 = 2;
	private static final int HEIGHT1 = 3;
	private static final long ID21 = 21;
	private static final int SIZE1 = 2;
	private static final long ID12 = 12;
	private static final int SIZE2 = 3;
	private static final long ID22 = 22;
	private static final int SIZE3 = 4;
	Shape shape11 = new Rectangle(ID11, WIDTH1, HEIGHT1);
	Shape shape21 = new Square(ID21, SIZE1);//add [1, 2]
	Shape shape12 = new Square(ID12, SIZE2);
	Shape shape22 = new Square(ID22, SIZE3);
	Canvas canvas1;
	Canvas canvas2;
	Shape[] shapes1;
	Shape[] shapes2; 
	Page page;
	
	@BeforeEach
	void setUp() {
		shapes2 = new Shape[]{shape21, shape22};		
		canvas2 = getCanvas(2, shapes2);
		shapes1 = new Shape[]{shape11, shape21, canvas2};
		canvas1 = getCanvas(1, shapes1);		
		Shape[] pageShapes = new Shape[] {shape11, shape21, canvas1, shape12, shape22};
		page = getPage(pageShapes);
	}
	
	private Canvas getCanvas(long id, Shape[] shapes) {
		Canvas result = new Canvas(id);
		for(Shape shape: shapes) {
			result.addShape(shape);
		}
		
		return result;
	}
	
	private Page getPage(Shape[] shapes) {
		Page result = new Page();
		for(Shape shape: shapes) {
			result.addShape(shape);
		}
		
		return result;
	}
	
	@Test
	void testAddShapeShape() {
		Shape newShape = new Rectangle(101, WIDTH1, HEIGHT1);
		page.addShape(newShape);
		assertThrowsExactly(ShapeAlreadyExistsException.class, () -> page.addShape(newShape));
	}

	@Test
	void testAddShapeLongArrayShape() {
		Shape newShape = new Rectangle(101, WIDTH1, HEIGHT1);
		page.addShape(new Long[] {1L, 2L}, newShape);
		assertThrowsExactly(ShapeAlreadyExistsException.class, 
				() -> page.addShape(new Long[] {1L, 2L}, newShape));
		assertThrowsExactly(NoCanvasException.class, 
				() -> page.addShape(new Long[] {12L}, newShape));
	}

	@Test
	void testRemoveShapeLong() {
		page.removeShape(ID11);
		assertEquals(null, page.removeShape(ID11));
	}

	@Test
	void testRemoveShapeLongArrayLong() {
		page.removeShape(new Long[] {1L}, 2L);
		assertThrowsExactly(ShapeNotFoundException.class, 
				() -> page.removeShape(new Long[] {2L}, canvas2.getId()));

		assertThrowsExactly(NoCanvasException.class, 
				() -> page.removeShape(new Long[] {12L}, 22L));
		assertNull(page.removeShape(new Long[] {1L}, canvas2.getId()));
	}

	@Test
	void testIterator() {
		Shape[] pageShapes = new Shape[] {shape11, shape21, canvas1, shape12, shape22};
		Page newPage = getPage(pageShapes);
		runTest(pageShapes, newPage);
		runTest(shapes1, canvas1);
		runTest(shapes2, canvas2);
		Canvas canvas = new Canvas(123);
		Iterator<Shape> it = canvas.iterator();
		assertThrowsExactly(NoSuchElementException.class, it::next);
	}
	
	private void runTest(Shape[] shapesExpected, Canvas canvas) {
		int index = 0;
		for(Shape shape: canvas) {
			assertEquals(shapesExpected[index++].getId(), shape.getId());
		}
		assertEquals(shapesExpected.length, index);		
	}
	
	private void runTest(Shape[] shapesExpected, Page page) {
		int index = 0;
		for(Shape shape: page) {
			assertEquals(shapesExpected[index++].getId(), shape.getId());
		}
		assertEquals(shapesExpected.length, index);		
	}

}
