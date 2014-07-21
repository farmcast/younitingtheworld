package com.ijoomer.weservice;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
 /**
  * A Class Used By Call Web Service With Custom Multipart Entity.
  * @author tasol
  *
  */
public class CustomMultiPartEntity extends MultipartEntity
{
 
	private  ProgressListener listener;
 
	/**
	 * Constructor
	 * @param listener {@link ProgressListener}
	 */
	public CustomMultiPartEntity(final ProgressListener listener)
	{
		super();
		this.listener = listener;
	}
 
	/**
	 * Constructor
	 * @param mode {@link HttpMultipartMode}
	 * @param listener {@link ProgressListener}
	 */
	public CustomMultiPartEntity(final HttpMultipartMode mode, final ProgressListener listener)
	{
		super(mode);
		this.listener = listener;
	}
 
	
	/**
	 * Constructor
	 * @param mode {@link HttpMultipartMode}
	 * @param boundary {@link String}
	 * @param charset {@link Charset}
	 * @param listener {@link ProgressListener}
	 */
	public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary, final Charset charset, final ProgressListener listener)
	{
		super(mode, boundary, charset);
		this.listener = listener;
	}
 
	@Override
	public void writeTo(final OutputStream outstream) throws IOException
	{
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}
 
	
 
	public static class CountingOutputStream extends FilterOutputStream
	{
 
		private final ProgressListener listener;
		private long transferred;
 
		/**
		 * Constructor
		 * @param out {@link OutputStream}
		 * @param listener {@link ProgressListener}
		 */
		public CountingOutputStream(final OutputStream out, final ProgressListener listener)
		{
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}
 
		/**
		 * A Method Is Used To Write 
		 * @param  {@link Byte}
		 * @param  {@link integer}
		 * @param {@link integer}
		 * @Exception {@link IOException}
		 */
		public void write(byte[] b, int off, int len) throws IOException
		{
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}
 
		/**
		 * A Method Is Used To Write
		 * @param {@link integer}
		 * @Exception {@link IOException}
		 */
		public void write(int b) throws IOException
		{
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}
}
