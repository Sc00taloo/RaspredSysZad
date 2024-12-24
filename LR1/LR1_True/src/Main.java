import mpi.*;

public class Main {
    public static void main(String[] args){
        int myrank, size, message;
        int tag = 1;
        MPI.Init(args);
        myrank = MPI.COMM_WORLD.Rank();
        size = MPI.COMM_WORLD.Size();
        message = myrank;
        int[] buffer = {message};
        if((myrank % 2) == 0){
            if((myrank + 1) != size){
                MPI.COMM_WORLD.Send(buffer, 0,1, MPI.INT, myrank+1, tag);
                System.out.println("Процесс " + myrank + " отпралено сообщение: " + message + " для процесса " + (myrank+1) );
            }
        } else{
            int[] new_message = new int[1];
            MPI.COMM_WORLD.Recv(new_message,0,1, MPI.INT, myrank-1, tag);
            System.out.println("Процесс " + myrank + " получено сообщение: " + new_message[0] + " от процесса " + (myrank-1) );
        }
        MPI.Finalize();
    }
}