
/**
 * Write a description of class SavingFunctions here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class SavingFunctions
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class SavingFunctions
     */
    public SavingFunctions()
    {
        // initialise instance variables
        x = 0;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public int sampleMethod(int y)
    {
        // put your code here
        return x + y;
    }
    public String saveDrawing(){
        drawingView.setDrawingCacheEnabled(true);
        Bitmap b = drawingView.getDrawingCache();

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // set the path to storage
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir and store the file there. Each new drawing will overwrite the previous
        File mypath=new File(directory,"drawn_image.jpg");

        //use a fileOutputStream to write the file to the location in a try / catch block
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    
    private void loadImageFromStorage(String path)
    {

        //use a fileInputStream to read the file in a try / catch block
        try {
            File f=new File(path, "drawn_image.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.outputView);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
