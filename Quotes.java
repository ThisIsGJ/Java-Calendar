import java.util.ArrayList;
import java.util.Random;

public class Quotes {
	private ArrayList<String> quotes = new ArrayList<String>();
	private ArrayList<UsedQuote> usedQuotes = new ArrayList<UsedQuote>();
	
	public Quotes() {
		populateQuotes();
	}
	private void populateQuotes() {
	// Quotes pulled from http://www.inspirational-quotes.info/
		quotes.add("One must be a wise reader to quote wisely and well."); quotes.add("To select well among old things, is almost equal to inventing new ones.");
		quotes.add("I have gathered a posie of other men's flowers, and nothing but the thread that binds them is my own."); quotes.add("Proverbs are mental gems gathered in the diamond districts of the mind.");
		quotes.add("I am but a gatherer and disposer of other men's stuff."); quotes.add("Stealing someone else's words frequently spares the embarrassment of eating your own.");
		quotes.add("A short saying oft contains much wisdom."); quotes.add("It often happens that the quotations constitute the most valuable part of a book.");
		quotes.add("A collection of rare thoughts is nothing less than a cabinet of intellectual gems."); quotes.add("Brevity is the soul of wit.");
		quotes.add("Good sayings are like pearls strung together."); quotes.add("A proverb is to speech what salt is to food.");
		quotes.add("Better one line that will survive the author than a hundred books outlived."); quotes.add("Proverbs are the cream of a nation's thought.");
		quotes.add("A proverb is much matter decocted into few words."); quotes.add("A proverb is much light condensed in one flash.");
		quotes.add("The proverb answers where the sermon fails."); quotes.add("Human success is a quotation from overhead.");
		quotes.add("Few maxims are true from every point of view."); quotes.add("Maxims are the condensed good sense of nations.");
		quotes.add("A proverb is a wise saying, old yet radiant with novelty. "); quotes.add("Nature and wisdom never are at strife.");
		quotes.add("It is easier to be wise for others than for ourselves."); quotes.add("The art of being wise is knowing what to overlook.");
		quotes.add("Wisdom is better than gold or silver."); quotes.add("Years teach us more than books.");
		quotes.add("It is best to learn wisdom by the experience of others."); quotes.add("A wise man learns by the mistakes of others, a fool by his own.");
		quotes.add("Silence does not always mark wisdom."); quotes.add("Measure a thousand times and cut once.");
		quotes.add("No man was ever wise by chance."); quotes.add("A loving heart is the truest wisdom.");
	}
	public ArrayList<String> getQuotes() {
		return quotes;
	}
	public String getQuote(int i) {
		return quotes.get(i);
	}
	public String getRandomQuote() {
		int i = 0;
		String quote;
		while (true) {
			Random myRand = new Random();
			quote = quotes.get(myRand.nextInt(quotes.size()));
			if (!isUsed(quote)) {
				break;
			}
			if (i == quotes.size()) {
				return "Could not find an unused quote";
			}
		}
		usedQuotes.add(new UsedQuote(quote, System.currentTimeMillis()));
		return quote;
	}
	public String getRandomQuoteNotThis(String quote) {
		String newQuote = getRandomQuote();
		while (newQuote.equals(quote)) {
			newQuote = getRandomQuote();
		}
		return newQuote;
	}
	
	public boolean isUsed(String quote) {
		for (int i = 0; i < usedQuotes.size(); i++) {
			if (usedQuotes.get(i).getQuote().equals(quote)) {
				return true;
			}
		}
		return false;
	}

	public class UsedQuote {
		private String quote;
		private long date;
		public UsedQuote(String quote, long date) {
			this.quote = quote;
			this.date = date;
		}
		public long getTime() {
			return date;
		}
		public String getQuote() {
			return quote;
		}
	}
		
}