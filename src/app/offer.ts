export interface Offer {
  id: number;
  name: string;
  description: string;
  offererId: number;
  time: Date;
  duration: number;
  maxNumOfParticipants: number;
  cancellationDeadline: number;
  status: string;
}
