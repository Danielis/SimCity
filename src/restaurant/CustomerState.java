package restaurant;

public enum CustomerState
{
	waiting, seated, readyToOrder, hasOrdered,
	ordering, reordering, reordered, finishedOrdering, 
	orderReady, eating, leaving, hasLeft, needsCheck,
	checkComputed,
};