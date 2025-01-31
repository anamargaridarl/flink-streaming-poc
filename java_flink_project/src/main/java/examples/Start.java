package examples;

public class Start {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Number of arguments provided is wrong! - " + args.length);
            System.out.println("Usage: java CommandLineArgsExample <exampleNumber> ...");
            return;
        }


        int exampleNumber = Integer.parseInt(args[0]);
        String[] arg = {"localhost:29092"};

        switch (exampleNumber) {
            /*case 1:
                SimpleKafkaPipeline w = new SimpleKafkaPipeline();
                w.transformStringKafka(arg);
                break;*/
            case 2:
                SimpleKeyBy a = new SimpleKeyBy();
                a.run();
            case 3:
                SimpleKeyedState b = new SimpleKeyedState();
                b.run();
            case 4:
                CharacterCounter c = new CharacterCounter();
                c.run();
            /*case 5:
                AvroSerialization av = new AvroSerialization();
                av.transformStringKafka(arg);*/
        }

    }
}