package com.company;
import java.io.*;
import java.util.Scanner;
import java.lang.Thread;
public class Audit {
    public static void main(String[] args) throws InterruptedException, Exception {
        File dir1 = new File("D:\\JAVA\\ideaproject");
        File dir2 = new File("D:\\javaproject");
        File dir3 = new File("D:\\Webdemo\\ServletTest\\src\\com\\zw\\Test\\JdbcTest.java");
        DirectoryCount D1 = new DirectoryCount(dir1);
        Thread t1 = new Thread(D1);
        DirectoryCount D2 = new DirectoryCount(dir2);
        Thread t2 = new Thread(D2);
        DirectoryCount D3 = new DirectoryCount(dir3);
        Thread t3 = new Thread(D3);
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println(dir1.getName()+" 这个文件或者文件夹中有 "+ D1.getLines()+" 行代码");
        System.out.println(dir2.getName()+" 这个文件或者文件夹中有 "+ D2.getLines()+" 行代码");
        System.out.println(dir3.getName()+" 这个文件或者文件夹中有 "+ D3.getLines()+" 行代码");
    }
}
//每个线程计算对应目录的代码行数
class DirectoryCount implements Runnable{
    int nums;
    File myfile;
    //通过指定的目录来构造线程类
    DirectoryCount(File f){
        this.nums = 0;
        this.myfile = f;
    }
    //如果是文件则直接读取其行数，否则就递归求解
    public  int Read(File file){
        int sum = 0;
        if(file==null)return 0;
        if(file.isFile()){
            try {
                sum += fileRead(file);
            }catch (IOException e){
                e.printStackTrace();
            }
            return sum;
        }
        //如果是目录
        else{
            File [] files = file.listFiles();
            for(int i=0;i<files.length;i++){
                sum +=Read(files[i]);
            }
            return sum;
        }
    }
    //重写run方法 调用Read方法
    public void run(){
        this.nums = Read(this.myfile);
    }
    public int getLines(){
        return this.nums;
    }
    //用来读取.java .c .py .cpp .go .html .js文件的行数
    public static int fileRead(File f)throws IOException{

        String filetype = f.getName(); //获取文件名
        if(filetype.endsWith(".java")||filetype.endsWith(".py")||filetype.endsWith(".c")||filetype.endsWith(".cpp")||filetype.endsWith(".go")
                ||filetype.endsWith(".html")||filetype.endsWith(".js")){
            int sum = 0;
            FileReader read = new FileReader(f);
            LineNumberReader reader = new LineNumberReader(read);
            while ((reader.readLine())!=null){
                sum++;
            }
            reader.close();//关闭资源
            read.close();
            return sum;
        }
        else
            return 0;
    }
}
