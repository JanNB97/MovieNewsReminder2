package com.yellowbite.movienewsreminder2.data;

import java.util.Comparator;
import java.util.logging.Logger;

import com.yellowbite.movienewsreminder2.data.Movie.Status;

public class MovieComparator implements Comparator<Movie>
{
    private final static int THIS_EARLIER = -1;     // >
    private final static int THIS_LATER = 1;    // <
    private final static int EQUAL = 0;

    public interface MovieComparable
    {
        int compareTo(Movie m1, Movie m2);
    }

    private static final MovieComparable compareEntliehenBis;
    private static final MovieComparable compareVorbestellungen;
    private static final MovieComparable compareZugang;
    private static final MovieComparable compareTitel;

    private static final MovieComparable thisLater;
    private static final MovieComparable thisEarlier;

    static
    {
        compareEntliehenBis = (Movie m1, Movie m2) -> m1.getEntliehenBis().compareTo(m2.getEntliehenBis());
        compareVorbestellungen = (Movie m1, Movie m2) -> m1.getVorbestellungen() - m2.getVorbestellungen();
        compareZugang = (Movie m1, Movie m2) -> {
            if(m1.getZugang() == null && m2.getZugang() == null)
            {
                return EQUAL;
            }

            if(m1.getZugang() == null)
            {
                return THIS_LATER;
            }

            if(m2.getZugang() == null)
            {
                return THIS_EARLIER;
            }

            return m1.getZugang().compareTo(m2.getZugang()) * -1;
        };

        compareTitel = (Movie m1, Movie m2) -> {
            if(m1.getTitel() == null && m2.getTitel() == null)
            {
                return EQUAL;
            }

            if(m1.getTitel() == null)
            {
                return THIS_LATER;
            }

            if(m2.getTitel() == null)
            {
                return THIS_EARLIER;
            }

            return m1.getTitel().compareTo(m2.getTitel());
        };

        thisLater = (Movie m1, Movie m2) -> THIS_LATER;
        thisEarlier = (Movie m1, Movie m2) -> THIS_EARLIER;
    }

    @Override
    public int compare(Movie m1, Movie m2)
    {
        if(m1.getStatus() == null && m2.getStatus() == null)
        {
            Logger.getGlobal().severe(m2.getTitel() + " and " + m1.getTitel() + ": status is null");
            return EQUAL;
        }

        if(m1.getStatus() == null)
        {
            Logger.getGlobal().severe(m1.getTitel() + ": status is null");
            return THIS_LATER;
        }

        if(m2.getStatus() == null)
        {
            Logger.getGlobal().severe(m2.getTitel() + ": status is null");
            return THIS_EARLIER;
        }

        switch (m1.getStatus())
        {
            case VERFUEGBAR:
                if(m2.getStatus() == Status.VERFUEGBAR)
                {
                    try
                    {
                        return this.compareTo(m1, m2,
                                /* Last */          compareTitel,
                                /* First checked */ compareZugang);
                    } catch (Exception e)
                    {
                        Logger.getGlobal().severe("Something went wrong");
                        return EQUAL;
                    }
                }
                return THIS_EARLIER;

            case ENTLIEHEN:
                switch (m2.getStatus())
                {
                    case VERFUEGBAR:
                        return THIS_LATER;
                    case ENTLIEHEN:
                        return this.compareTo(m1, m2,
                                /* Last */          compareTitel,
                                /* First checked */ compareVorbestellungen, compareEntliehenBis, compareZugang);
                    case VORBESTELLT:
                        return this.compareTo(m1, m2,
                                /* Last */          thisEarlier,
                                /* First checked */ compareVorbestellungen);
                    case IN_BEARBEITUNG:
                        return THIS_EARLIER;
                }

            case VORBESTELLT:
                switch (m2.getStatus())
                {
                    case VERFUEGBAR:
                        return THIS_LATER;
                    case ENTLIEHEN:
                        return this.compareTo(m1, m2,
                                /* Last */          thisLater,
                                /* First checked */ compareVorbestellungen);
                    case VORBESTELLT:
                        return this.compareTo(m1, m2,
                                /* Last */          compareTitel,
                                /* First checked */ compareVorbestellungen, compareZugang);
                    case IN_BEARBEITUNG:
                        return THIS_EARLIER;
                }

            case IN_BEARBEITUNG:
                if(m2.getStatus() != Status.IN_BEARBEITUNG)
                {
                    return THIS_LATER;
                }

                return this.compareTo(m1, m2,
                        /* Last */          compareTitel,
                        /* First checked */ compareVorbestellungen);
        }

        Logger.getGlobal().severe("Something went wrong: Reached unreachable statement");
        return EQUAL;
    }

    private int compareTo(Movie m1, Movie m2, MovieComparable lastCompare, MovieComparable ... compares)
    {
        for(MovieComparable callable : compares)
        {
            try
            {
                int i = callable.compareTo(m1, m2);

                if(i != 0)
                {
                    return i;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            return lastCompare.compareTo(m1, m2);
        } catch (Exception e)
        {
            Logger.getGlobal().severe("Something went wrong");
            return 0;
        }
    }
}
