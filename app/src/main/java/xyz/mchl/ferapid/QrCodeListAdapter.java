package xyz.mchl.ferapid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.mchl.ferapid.persistence.QRCode;

public class QrCodeListAdapter extends RecyclerView.Adapter<QrCodeListAdapter.QrCodeViewHolder>
{
    private List<QRCode> qrCodeList;
    private View.OnLongClickListener longClickListener;

    public QrCodeListAdapter(List<QRCode> codeList, View.OnLongClickListener longClickListener) {
        this.qrCodeList = codeList;
        this.longClickListener = longClickListener;
    }

    public QrCodeListAdapter(List<QRCode> codeList) {
        this.qrCodeList = codeList;
        this.longClickListener = null;
    }

    @Override
    public QrCodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.qrcode_item, parent, false);
        return new QrCodeViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return qrCodeList.size();
    }

    @Override
    public void onBindViewHolder(QrCodeViewHolder holder, int position) {
        QRCode qrCode = qrCodeList.get(position);
        holder.accountNumberView.setText(qrCode.getAccountNumber());
    }

    public void addItems(List<QRCode> qrCodes) {
        this.qrCodeList = qrCodes;
        notifyDataSetChanged();
    }

    static class QrCodeViewHolder extends RecyclerView.ViewHolder {
        private TextView accountNumberView;

        QrCodeViewHolder(View view) {
            super(view);
            accountNumberView = view.findViewById(R.id.account_number);
        }
    }
}
