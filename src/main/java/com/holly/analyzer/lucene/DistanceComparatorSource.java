package com.holly.analyzer.lucene;

import java.io.IOException;

import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldComparatorSource;
import org.apache.lucene.search.SimpleFieldComparator;
import org.apache.lucene.util.BytesRef;

public class DistanceComparatorSource extends FieldComparatorSource {
	private int x;
	private int y;

	DistanceComparatorSource(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public FieldComparator<?> newComparator(String fieldName, int numHits, int sortPos, boolean reversed) {
		return new DistanceStoreComparator(fieldName, numHits, x, y);
	}

	private class DistanceStoreComparator<T> extends SimpleFieldComparator<String> {
		private int x;
		private int y;
		private final float[] values;
		private float bottom;
		private float topValue;
		private String fieldName;
		private BinaryDocValues binaryDocValues;

		public DistanceStoreComparator(String fieldName, int hits, int x, int y) {
			values = new float[hits];
			this.fieldName = fieldName;
			this.x = x;
			this.y = y;
		}

		@Override
		public int compare(int slot1, int slot2) {
			return Float.compare(values[slot1], values[slot2]);
		}

		private float getDistance(int doc) {
			BytesRef bytesRef = binaryDocValues.get(doc);
			String xy = bytesRef.utf8ToString();
			String[] array = xy.split(",");
			// 求横纵坐标差
			int deltax = Integer.parseInt(array[0]) - x;
			int deltay = Integer.parseInt(array[1]) - y;
			// 开平方根
			float distance = (float) Math.sqrt(deltax * deltax + deltay * deltay);
			// System.out.println(distance);
			return distance;
		}

		@Override
		public void setBottom(int slot) {
			bottom = values[slot];
		}

		@Override
		public int compareBottom(int doc) throws IOException {
			return Float.compare(bottom, getDistance(doc));
		}

		@Override
		public int compareTop(int doc) throws IOException {
			return Float.compare(topValue, getDistance(doc));
		}

		@Override
		public void copy(int slot, int doc) throws IOException {
			values[slot] = getDistance(doc);
		}

		@Override
		protected void doSetNextReader(LeafReaderContext context) throws IOException {
			 binaryDocValues = context.reader().getBinaryDocValues(fieldName);
		}

		@Override
		public void setTopValue(String value) {
			topValue = Float.valueOf(value);
		}

		@Override
		public String value(int slot) {
			 return values[slot]+"";
		}

	}
}
