package Learn.innerClasses;

public class AnonymousClasses {

    public interface GreetingModule {
        void sayHello();
    }


    GreetingModule greeting = new GreetingModule() {
        @Override
        public void sayHello() {
            System.out.println("good morning");
        }
    };


    public static void main(String[] args) {
        AnonymousClasses ac = new AnonymousClasses();
        ac.greeting.sayHello();
    }

}