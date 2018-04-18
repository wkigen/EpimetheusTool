import com.github.wkigen.epimetheus.EpimetheusToolLib.builder.PatchBuilder;
import com.github.wkigen.epimetheus.EpimetheusToolLib.comparator.ApkComparator;
import org.jf.dexlib2.writer.builder.DexBuilder;

public class main {

	private static void printHelp(){
		System.out.printf("params:\n");
		System.out.printf("1.the absolute path to old apk \n");
		System.out.printf("2.the absolute path to new apk \n");
		System.out.printf("3.patch output folder \n");
		System.out.printf("4.the version of patch  \n");
	}
	
	public static void main(String[] args) {

		if(args == null || args.length != 4 ) {
			printHelp();
			return;
		}

		ApkComparator apkComparator = new ApkComparator();
		apkComparator.compare(args[0], args[1]);

		PatchBuilder patchBuilder = new PatchBuilder();
		patchBuilder.OutPutPatch(args[2]).Version(args[3]).DexBuilder(apkComparator.dexComparator.getChangeClassList()).Build();

	}
	
	
}
