package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Doctors {
    private Connection connection;
    private Scanner sc = new Scanner(System.in);

    Doctors(Connection connection) {
        this.connection = connection;
    }

    public void viewDoctors() {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM doctors")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Doctor ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Specialization: " + rs.getString("specialization"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctor() {
        System.out.print("Enter Doctor ID to update: ");
        int id = sc.nextInt();
        System.out.print("Enter new Doctor Name: ");
        String name = sc.next();
        System.out.print("Enter new Specialization: ");
        String specialization = sc.next();

        try (PreparedStatement stmt = connection.prepareStatement("UPDATE doctors SET name = ?, specialization = ? WHERE id = ?")) {
            stmt.setString(1, name);
            stmt.setString(2, specialization);
            stmt.setInt(3, id);
            if (stmt.executeUpdate() > 0) {
                System.out.println("Doctor Updated Successfully");
            } else {
                System.out.println("Failed to Update Doctor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDoctor() {
        System.out.print("Enter Doctor ID to delete: ");
        int id = sc.nextInt();

        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM doctors WHERE id = ?")) {
            stmt.setInt(1, id);
            if (stmt.executeUpdate() > 0) {
                System.out.println("Doctor Deleted Successfully");
            } else {
                System.out.println("Failed to Delete Doctor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchDoctorByName() {
        System.out.print("Enter Doctor Name: ");
        String name = sc.next();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM doctors WHERE name LIKE ?")) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Doctor ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Specialization: " + rs.getString("specialization"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM doctors WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
