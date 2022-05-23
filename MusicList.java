package TongHop;

import ThiJAVA.database.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MusicList {
    private String url = "jdbc:mysql://localhost:3306/emusicstore";
    private String user = "root";
    private String password = "";

    Scanner sc = new Scanner(System.in);
    List<Music> musicList;

    public MusicList() {
        this.musicList = new ArrayList<>();
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public List<Music> arrayListMusic() throws  SQLException {
        String strSelect = "select * from musics";
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();;
            ResultSet rs = statement.executeQuery(strSelect);
            while (rs.next()) {
                Music music = new Music(rs.getString("id"),rs.getString("name"),rs.getString("author"),rs.getString("year"));
                if (!this.musicList.contains(music)) {
                    musicList.add(music);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return musicList;
    }





    //select
    public void display() throws  SQLException {
        arrayListMusic();
        int num = musicList.size();

        System.out.printf("%-25s", "StudentID");
        System.out.printf("%-25s", "Student Name");
        System.out.printf("%-25s", "Address");
        System.out.printf("%-25s", "Phone");
        System.out.println();

        for (int i = 0; i < num ; i++) {
            System.out.printf("%-25s", musicList.get(i).getId());
            System.out.printf("%-25s", musicList.get(i).getName());
            System.out.printf("%-25s", musicList.get(i).getAuthor());
            System.out.printf("%-25s", musicList.get(i).getYear());
            System.out.println();
        }
    }

    // search
    public void searchId(int id) throws SQLException{
//        arrayListMusic();
        for (Music music: musicList) {
            if (music.equals(id)) {
                System.out.printf("%-25s", "StudentID");
                System.out.printf("%-25s", "Student Name");
                System.out.printf("%-25s", "Address");
                System.out.printf("%-25s", "Phone");
                System.out.println();

                System.out.printf("%-25s", music.getId());
                System.out.printf("%-25s", music.getName());
                System.out.printf("%-25s", music.getAuthor());
                System.out.printf("%-25s", music.getYear());
            }
        }
    }

    public void searchName(String name) throws SQLException{
        arrayListMusic();
        for (Music music: musicList) {
            if (music.getName().indexOf(name) >= 0) {
                System.out.printf("%-25s", "StudentID");
                System.out.printf("%-25s", "Student Name");
                System.out.printf("%-25s", "Address");
                System.out.printf("%-25s", "Phone");
                System.out.println();

                System.out.printf("%-25s", music.getId());
                System.out.printf("%-25s", music.getName());
                System.out.printf("%-25s", music.getAuthor());
                System.out.printf("%-25s", music.getYear());
            }
        }
    }

    //Update
    public  void updateStu() {
        System.out.println("Nhập ID bài hát cần chỉnh sửa ");
        String id = sc.nextLine();
        if (musicList.stream().anyMatch(music -> music.getId().equals(id)) ){
            for (Music stu  : musicList) {
                if (stu.getId().equals(id)){
                    System.out.println("Mời bạn nhập lựa chọn");
                    System.out.println("1.Để sửa name");
                    System.out.println("2. Để sửa author");
                    int sl = sc.nextInt();
                    switch (sl){
                        case 1:
                            System.out.println("Mời bạn nhập tên bài hát mới");
                            sc.nextLine();
                            String name = sc.nextLine();
                            stu.setName(name);
                            System.out.println("Sửa Thành công");
                            break;

                        case 2:
                            System.out.println("Mời bạn nhập tác giả mới");
                            sc.nextLine();
                            String author = sc.nextLine();
                            stu.setAuthor(author);
                            System.out.println("Sửa Thành công");
                            break;
                        default:
                            System.out.println("Nhập sai lựa chọn");
                            break;

                    }
                }
            }

        }else {
            System.out.println("Tên bài hát ko tồn tại trong bộ sưu tập");
        }
    }

    public void saveStu() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/students", "root", "");
            connection.setAutoCommit(false);
            String insert = "insert into student value(?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(insert);
            for (Music stu : musicList) {
                System.out.println(stu.getId());
                ps.setString(1,stu.getId());
                ps.setString(2,stu.getName());
                ps.setString(3,stu.getAuthor());
                ps.setString(4,stu.getYear());
                ps.addBatch();
            }
                int[] count = ps.executeBatch();
                int sum = 0;
                for (int i : count){
                    sum += i;
                }
                connection.commit();
            System.out.println("Số bản ghi đc thêm thành công: " + sum);

        }catch (SQLException e){
            System.out.println("Thêm dữ liệu thất bại");
            e.printStackTrace();

        }
    }


}
