package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patients {
    private Connection connection;
    private Scanner sc;

    Patients(Connection connection, Scanner sc) {
        this.connection = connection;
        this.sc = sc;
    }

    public void addPatient() {
        System.out.print("Enter Patient Name: ");
        String name = sc.next();
        System.out.print("Enter Patient Age: ");
        int age = sc.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = sc.next();

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            if (stmt.executeUpdate() > 0) {
                System.out.println("Patientadded successfully.");
            } else {
                System.out.println("Failed to add patient.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void viewPatients() {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM patients")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Patient ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        int id = sc.nextInt();
        System.out.print("Enter new Patient Name: ");
        String name = sc.next();
        System.out.print("Enter new Age: ");
        int age = sc.nextInt();
        System.out.print("Enter new Gender: ");
        String gender = sc.next();

        try (PreparedStatement stmt = connection.prepareStatement("UPDATE patients SET name = ?, age = ?, gender = ? WHERE id = ?")) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setInt(4, id);
            if (stmt.executeUpdate() > 0) {
                System.out.println("Patient updated successfully.");
            } else {
                System.out.println("Failed to update patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        int id = sc.nextInt();

        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM patients WHERE id = ?")) {
            stmt.setInt(1, id);
            if (stmt.executeUpdate() > 0) {
                System.out.println("Patient deleted successfully.");
            } else {
                System.out.println("Failed to delete patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchPatientByName() {
        System.out.print("Enter Patient Name: ");
        String name = sc.next();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM patients WHERE name LIKE ?")) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Patient ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM patients WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
