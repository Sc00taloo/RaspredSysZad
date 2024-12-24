import mpi.*;

public class Main {
    public static void main(String[] args) {
        int myrank, size;
        int tag = 1;
        MPI.Init(args);
        myrank = MPI.COMM_WORLD.Rank();
        size = MPI.COMM_WORLD.Size();
        int[] new_message = new int[1];
        int[] message = new int[1];
        message[0] = myrank;

//        //Блокирующий
//        if (size > 1){
//            if (myrank == 0){
//                // Отправка и получение значения
//                System.out.println("Процессор " + myrank + " отправляет " + message[0] +  " процессору " + (myrank + 1));
//                MPI.COMM_WORLD.Sendrecv(message, 0, 1, MPI.INT, (myrank + 1), tag,
//                        new_message, 0, 1, MPI.INT, size - 1, tag);
//                System.out.println("Процессор " + myrank + " получил " + new_message[0] + " от процессора " + (size-1));
//                // Суммируем полученное значение
//                System.out.println("Сумма: " + new_message[0]);
//            }
//            else{
//                int process = (myrank - 1 + size) % size;
//                int targetProcess = (myrank + 1) % size;
//                new_message[0] = message[0];
//                MPI.COMM_WORLD.Recv(message, 0, 1, MPI.INT, process, tag);
//                System.out.println("Процессор " + myrank + " получил " + message[0] + " от процесса " + process);
//                new_message[0] += message[0];
//                System.out.println("Процессор " + myrank + " отправляет " + new_message[0] + " процессору " + targetProcess);
//                MPI.COMM_WORLD.Send(new_message, 0, 1, MPI.INT, targetProcess, tag);
//            }
//        }

        //Неблокирующий
        Request request;
        if (size > 1) {
            if (myrank == 0) {
                System.out.println("Процессор " + myrank + " отправляет " + message[0] + " процессору " + (myrank + 1));
                request = MPI.COMM_WORLD.Isend(message, 0, 1, MPI.INT, myrank+1, tag);
                request = MPI.COMM_WORLD.Irecv(new_message, 0, 1, MPI.INT, size-1, tag);

                request.Wait();
                System.out.println("Процессор " + myrank + " получил " + new_message[0] + " от процессора " + (size-1));
                System.out.println("Сумма: " + new_message[0]);
            } else {
                int process = (myrank - 1 + size) % size;
                int targetProcess = (myrank + 1) % size;
                new_message[0] = message[0];
                request = MPI.COMM_WORLD.Irecv(message, 0, 1, MPI.INT, process, tag);
                request.Wait();
                System.out.println("Процессор " + myrank + " получил " + message[0] + " от процессора " + process);
                new_message[0] += message[0];
                request = MPI.COMM_WORLD.Isend(new_message, 0, 1, MPI.INT, targetProcess, tag);
                System.out.println("Процессор " + myrank + " отправляет " + new_message[0] + " процессору " + targetProcess);
            }
        }
        MPI.Finalize();
    }
}