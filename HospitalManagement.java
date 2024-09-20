package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagement {
    private static final String url = System.getenv("DB_URL");
    private static final String username = System.getenv("DB_USERNAME");
    private static final String password = System.getenv("DB_PASSWORD");

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Patients patients = new Patients(connection, sc);
            Doctors doctors = new Doctors(connection);

            while (true) {
                System.out.println("--- HOSPITAL MANAGEMENT SYSTEM ---");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. Update Patient");
                System.out.println("4. Delete Patient");
                System.out.println("5. Search Patient by Name");
                System.out.println("6. View Doctor");
                System.out.println("7. Update Doctor");
                System.out.println("8. Delete Doctor");
                System.out.println("9. Search Doctor by Name");
                System.out.println("10. Book Appointment");
                System.out.println("11. Cancel Appointment");
                System.out.println("12. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1: patients.addPatient(); break;
                    case 2: patients.viewPatients(); break;
                    case 3: patients.updatePatient(); break;
                    case 4: patients.deletePatient(); break;
                    case 5: patients.searchPatientByName(); break;
                    case 6: doctors.viewDoctors(); break;
                    case 7: doctors.updateDoctor(); break;
                    case 8: doctors.deleteDoctor(); break;
                    case 9: doctors.searchDoctorByName(); break;
                    case 10: BookAppoint(patients, doctors, connection, sc); break;
                    case 11: cancelAppointment(connection, sc); break;
                    case 12: System.out.println("Thank you for using HOSPITAL MANAGEMENT SYSTEM"); return;
                    default: System.out.println("Enter a valid choice (1-12)");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void BookAppoint(Patients patients, Doctors doctors, Connection connection, Scanner sc) {
        System.out.print("Enter Patient Id: ");
        int pid = sc.nextInt();
        System.out.print("Enter Doctor Id: ");
        int did = sc.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String appointDate = sc.next();

        if (patients.getPatientById(pid) && doctors.getDoctorById(did)) {
            if (checkDoctorAvailability(appointDate, did, connection)) {
                try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO appointments (PID, DID, appointment_date) VALUES (?, ?, ?)")) {
                    stmt.setInt(1, pid);
                    stmt.setInt(2, did);
                    stmt.setString(3, appointDate);
                    if (stmt.executeUpdate() > 0) {
                        System.out.println("Appointment Booked Successfully");
                    } else {
                        System.out.println("Failed to Book Appointment");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor is not available on this date.");
            }
        } else {
            System.out.println("Invalid Patient or Doctor ID.");
        }
    }

    public static void cancelAppointment(Connection connection, Scanner sc) {
        System.out.print("Enter Appointment ID to cancel: ");
        int appointmentId = sc.nextInt();
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM appointments WHERE id = ?")) {
            stmt.setInt(1, appointmentId);
            if (stmt.executeUpdate() > 0) {
                System.out.println("Appointment Cancelled Successfully");
            } else {
                System.out.println("Failed to Cancel Appointment. Check Appointment ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkDoctorAvailability(String appointDate, int did, Connection connection) {
        String query = "SELECT count(*) FROM appointments WHERE DID = ? AND appointment_date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, did);
            stmt.setString(2, appointDate);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
